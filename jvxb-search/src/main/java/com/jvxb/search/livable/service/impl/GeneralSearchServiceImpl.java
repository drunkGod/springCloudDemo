package com.jvxb.search.livable.service.impl;

import com.jvxb.common.utils.BeanUtil;
import com.jvxb.common.utils.Cu;
import com.jvxb.search.configuration.exception.EsSearchException;
import com.jvxb.search.livable.entity.EsDocument;
import com.jvxb.search.livable.entity.SearchResult;
import com.jvxb.search.livable.service.GeneralSearchService;
import com.jvxb.search.utils.EsConditionResolve;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jvxb
 * @since 2020-06-16
 */
@Service
public class GeneralSearchServiceImpl implements GeneralSearchService {

    private static final Logger logger = LoggerFactory.getLogger(GeneralSearchServiceImpl.class);

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void saveDoc(EsDocument esDocument) {
        String index = esDocument.getIndex();
        String type = esDocument.getType();
        Map document = esDocument.getDocument();
        if (Cu.isNullOrEmpty(index, type, document)) {
            return;
        }

        Client client = elasticsearchTemplate.getClient();
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex(index, type).setSource(document);
        if (document.get("id") != null) {
            indexRequestBuilder.setId(document.get("id").toString());
        }
        IndexResponse response = indexRequestBuilder.get();
        RestStatus status = response.status();
        //返回状态有两种情况-第一:该文档id不存在,进行创建写入RestStatus.CREATED ; 第二,文档id已经存在,进行更新操作返回RestStatus.OK
        if (status != RestStatus.CREATED && status != RestStatus.OK) {
            logger.error(String.format("创建索引出错。index:% type:%s id:%s status:%s", index, type, document.get("id"), status));
            throw new EsSearchException(EsSearchException.SEARCH_EXCEPTION.ES_ADD_ERROR);
        }
    }

    @Override
    public void batchSaveDoc(List<EsDocument> documentList) {
        Client client = elasticsearchTemplate.getClient();
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        for (EsDocument esDocument : documentList) {
            Map<String, Object> documentMap = BeanUtil.bean2Map(esDocument.getDocument());
            bulkRequestBuilder.add(client.prepareIndex(esDocument.getIndex(), esDocument.getType())
                    .setId(documentMap.get("id").toString())
                    .setSource(documentMap));
        }
        BulkResponse bulkResp = bulkRequestBuilder.get();
        if (bulkResp.hasFailures()) {
            logger.error(bulkResp.buildFailureMessage());
            throw new EsSearchException(EsSearchException.SEARCH_EXCEPTION.ES_ADD_ERROR);
        }
    }

    @Override
    public void deleteDoc(EsDocument esDocument) {
        Client client = elasticsearchTemplate.getClient();
        client.prepareDelete(esDocument.getIndex(), esDocument.getType(), esDocument.getDocument().get("id").toString()).get();
    }

    @Override
    public SearchResult query(String index, String type, String condition) {
        SearchResult searchResult = SearchResult.empty();
        Client client = elasticsearchTemplate.getClient();
        SearchRequestBuilder requestBuilder = client.prepareSearch(index).setTypes(type);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //如果有传参数，则根据参数解析查询条件。
        checkAndResolveConditoin(requestBuilder, boolQueryBuilder, condition, searchResult);
        //各种组合条件
        requestBuilder.setQuery(boolQueryBuilder);
        //单次查询超过1W时，需要使用scroll方式查询。否则容易造成OOM。
        int maxIndexWindowResult = 10000;
        if (searchResult.getPageNum() * searchResult.getPageSize() > maxIndexWindowResult) {
            return EsConditionResolve.queryInScroll(client, requestBuilder, searchResult, maxIndexWindowResult);
        }
        //普通from-to查询
        SearchHits searchHits = requestBuilder.get().getHits();
        if (searchHits.getTotalHits() == 0L) {
            return searchResult;
        }
        EsConditionResolve.convertSearchResonse2ResultList(searchHits, searchResult);
        return searchResult;
    }

    @Override
    public SearchResult queryByClient(String node, String index, String type, String condition) {
        RestHighLevelClient client = getClientByClientStr(node);
        if (client == null) {
            throw new EsSearchException("无法连接Es。node:" + node);
        }
        SearchResult searchResult = SearchResult.empty();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchRequest requestBuilder = new SearchRequest().indices(index).types(type);
        //如果有传参数，则根据参数解析查询条件。
        checkAndResolveConditoin(sourceBuilder, boolQueryBuilder, condition, searchResult);
        //各种组合条件
        requestBuilder.source(sourceBuilder.query(boolQueryBuilder));
        try {
            SearchResponse searchResponse = client.search(requestBuilder, RequestOptions.DEFAULT);
            SearchHits searchHits = searchResponse.getHits();
            long num = searchHits.getTotalHits();
            if (num == 0L) {
                return searchResult;
            }
            EsConditionResolve.convertSearchResonse2ResultList(searchHits, searchResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    @Override
    public Map queryById(String index, String type, String id) {
        Map resultMap = new HashMap();
        Client client = elasticsearchTemplate.getClient();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termQuery("id", id));
        SearchResponse searchResponse = client
                .prepareSearch(index)
                .setTypes(type)
                .setQuery(boolQueryBuilder).get();
        SearchHits searchHits = searchResponse.getHits();
        long num = searchHits.getTotalHits();
        if (num == 0L) {
            return null;
        } else if (num > 1) {
            throw new EsSearchException("多余一条记录被发现");
        }
        resultMap = searchHits.getHits()[0].getSourceAsMap();
        return resultMap;
    }

    private void checkAndResolveConditoin(SearchRequestBuilder requestBuilder, BoolQueryBuilder boolQueryBuilder, String condition, SearchResult searchResult) {
        if (Cu.notNullOrEmpty(condition)) {
            if (!BeanUtil.isJSONValid(condition)) {
                throw new EsSearchException("查询参数转换为Map异常。参数：" + condition);
            }
            EsConditionResolve.resolveQuery(requestBuilder, boolQueryBuilder, BeanUtil.jsonStr2Map(condition), searchResult);
        }
        System.out.println("boolQueryBuilder:" + boolQueryBuilder);
        System.out.println("requestBuilder:" + requestBuilder);
    }

    private void checkAndResolveConditoin(SearchSourceBuilder requestBuilder, BoolQueryBuilder boolQueryBuilder, String condition, SearchResult searchResult) {
        if (Cu.notNullOrEmpty(condition)) {
            if (!BeanUtil.isJSONValid(condition)) {
                throw new EsSearchException("查询参数转换为Map异常。参数：" + condition);
            }
            EsConditionResolve.resolveHighLevelQuery(requestBuilder, boolQueryBuilder, BeanUtil.jsonStr2Map(condition), searchResult);
        }
    }

    private RestHighLevelClient getClientByClientStr(String node) {
        if (Cu.isNullOrEmpty(node)) {
            return null;
        }
        String[] hosts = node.split(":");
        RestHighLevelClient client;
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(hosts[0], Integer.valueOf(hosts[1]), "http")
                ));
        return client;
    }
}
