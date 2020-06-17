package com.jvxb.search.utils;

import com.jvxb.common.utils.Cu;
import com.jvxb.search.configuration.exception.EsSearchException;
import com.jvxb.search.livable.entity.SearchResult;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 查询帮助类
 * @author jvxb
 * @since 2020-06-13
 */
public class QueryHelper {

    private static final Logger logger = LoggerFactory.getLogger(QueryHelper.class);

    /**
     * - 字段（field），对应内容有：eq(ual)（等于）、ne（不等于）、like（近似）等，
     * - 范围（range），对应内容有：between(ff/ft/tf)（介于）、lt(e)（小于(等于)）、gt(e)（大于(等于)）等，
     * - 排序（sort），对应内容有：desc（倒序）、asc（顺序），
     * - 高亮（highLight）、
     * - 分页（page），对应内容：起始页,每页数量,
     * - 获取内容（fetch/fetchSource），对应内容：includes（需要获取的字段）、excludes不需要获取的字段
     * 不需要的 key可以为空或 key对应value为null，实例如下：
     * {
     * "field":  {"field1": "equal v1", "field2": “like v2”},
     * "range": {"field1": "between v1,v2", "field2": “lte v2”, "field3": "gt v3"},
     * "sort": {"field1": “desc”, "field2": "asc"},
     * “hightLight”: null,
     * "page": "1,10"
     * "fetch": "id,name"，或者{"includes": ["id","name"], "excludes": null}、{"includes": null, "excludes": ["id","name"]}
     * }
     *
     * @param boolQueryBuilder
     * @param conditionMap
     */
    public static void resolveQuery(SearchRequestBuilder searchRequestBuilder,
                                    BoolQueryBuilder boolQueryBuilder,
                                    Map<String, Object> conditionMap,
                                    SearchResult searchResult) {
        for (Map.Entry<String, Object> entry : conditionMap.entrySet()) {
            String type = entry.getKey();
            Object value = entry.getValue();
            if (Cu.isNullOrEmpty(value)) {
                continue;
            }
            //根据查询条件分类组装。
            if (type.equalsIgnoreCase(QueryConstants.FIELD)) {
                resolveQueryField(boolQueryBuilder, value);
            } else if (type.equalsIgnoreCase(QueryConstants.HIGHTLIGHT)) {
                resolveQueryHightlight(boolQueryBuilder, value);
            } else if (type.equalsIgnoreCase(QueryConstants.RANGE)) {
                resolveQueryRange(searchRequestBuilder, value);
            } else if (type.equalsIgnoreCase(QueryConstants.PAGE)) {
                resolveQueryPage(searchRequestBuilder, value);
                setSearchResultPage(searchResult, value);
            } else if (type.equalsIgnoreCase(QueryConstants.SORT)) {
                resolveQuerySort(searchRequestBuilder, value);
            } else if (type.equalsIgnoreCase(QueryConstants.FETCH) || type.equalsIgnoreCase(QueryConstants.FETCHSOURCE)) {
                resolveQueryFetchSource(searchRequestBuilder, value);
            }
        }
    }


    private static void resolveQueryFetchSource(SearchRequestBuilder searchRequestBuilder, Object value) {
        if (Cu.isNullOrEmpty(value) && !(value instanceof CharSequence) && !(value instanceof Map)) {
            return;
        }
        if (value instanceof CharSequence) {
            String[] includes = Cu.convert2StrTrimArr(value);
            searchRequestBuilder.setFetchSource(includes, null);
        } else if (value instanceof Map) {
            Object includeValue = ((Map) value).get(QueryConstants.FETCH_INCLUDES);
            Object excludeValue = ((Map) value).get(QueryConstants.FETCH_EXCLUDES);
            String[] includes = Cu.convert2StrTrimArr(includeValue);
            String[] excludes = Cu.convert2StrTrimArr(excludeValue);
            searchRequestBuilder.setFetchSource(includes, excludes);
        }
    }

    private static void resolveQueryPage(SearchRequestBuilder searchRequestBuilder, Object value) {
        //处理分页 "page": "1,10"
        Optional.ofNullable(value).map(p -> p.toString()).ifPresent(e -> {
            int pageNum = Integer.valueOf(e.split(QueryConstants.COMMA)[0]);
            int pageSize = Integer.valueOf(e.split(QueryConstants.COMMA)[1]);
            searchRequestBuilder.setFrom(pageNum - 1).setSize(pageSize);
        });
    }

    private static void resolveQueryHightlight(BoolQueryBuilder boolQueryBuilder, Object value) {
    }

    //处理排序 "sort": {"field1": “desc”, "field2": "asc"},
    private static void resolveQuerySort(SearchRequestBuilder searchRequestBuilder, Object value) {
        if (Cu.isNullOrEmpty(value) || !(value instanceof Map)) {
            return;
        }
        for (Map.Entry<String, String> entry : ((Map<String, String>) value).entrySet()) {
            searchRequestBuilder.addSort(entry.getKey(), SortOrder.fromString(entry.getValue()));
        }
    }

    //处理范围 "range": {"field1": "between v1,v2", "field2": “lte v2”, "field3": "gt v3"},
    private static void resolveQueryRange(SearchRequestBuilder requestBuilder, Object value) {
        if (Cu.isNullOrEmpty(value) || !(value instanceof Map)) {
            return;
        }
        for (Map.Entry<String, String> entry : ((Map<String, String>) value).entrySet()) {
            String field = entry.getKey();
            String handleStr = entry.getValue().trim();
            String handle = handleStr.split("\\s+", 2)[0];
            String fieldValue = handleStr.split("\\s+", 2)[1];
            if (handle.equalsIgnoreCase(QueryConstants.BETWEEN)||handle.equalsIgnoreCase(QueryConstants.BETWEEN_TT)) {
                requestBuilder.setQuery(QueryBuilders.rangeQuery(field).from(fieldValue.split(QueryConstants.COMMA)[0]).to(fieldValue.split(QueryConstants.COMMA)[1]));
            } else if (handle.equalsIgnoreCase(QueryConstants.BETWEEN_FF)) {
                requestBuilder.setQuery(QueryBuilders.rangeQuery(field).from(fieldValue.split(QueryConstants.COMMA)[0], false).to(fieldValue.split(QueryConstants.COMMA)[1], false));
            } else if (handle.equalsIgnoreCase(QueryConstants.BETWEEN_FT)) {
                requestBuilder.setQuery(QueryBuilders.rangeQuery(field).from(fieldValue.split(QueryConstants.COMMA)[0], false).to(fieldValue.split(QueryConstants.COMMA)[1]));
            } else if (handle.equalsIgnoreCase(QueryConstants.BETWEEN_TF)) {
                requestBuilder.setQuery(QueryBuilders.rangeQuery(field).from(fieldValue.split(QueryConstants.COMMA)[0]).to(fieldValue.split(QueryConstants.COMMA)[1], false));
            } else if (handle.equalsIgnoreCase(QueryConstants.RANGE_LT)) {
                requestBuilder.setQuery(QueryBuilders.rangeQuery(field).lt(fieldValue));
            } else if (handle.equalsIgnoreCase(QueryConstants.RANGE_LTE)) {
                requestBuilder.setQuery(QueryBuilders.rangeQuery(field).lte(fieldValue));
            } else if (handle.equalsIgnoreCase(QueryConstants.RANGE_GT)) {
                requestBuilder.setQuery(QueryBuilders.rangeQuery(field).gt(fieldValue));
            } else if (handle.equalsIgnoreCase(QueryConstants.RANGE_GTE)) {
                requestBuilder.setQuery(QueryBuilders.rangeQuery(field).gte(fieldValue));
            }
        }
    }

    /**
     * value = {"field1": "equal v1", "field2": “like v2”},
     *
     * @param boolQueryBuilder
     * @param value
     */
    private static void resolveQueryField(BoolQueryBuilder boolQueryBuilder, Object value) {
        if (Cu.isNullOrEmpty(value) || !(value instanceof Map)) {
            return;
        }
        for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
            try {
                String field = entry.getKey();
                String handleStr = entry.getValue().toString().trim();
                String handle = handleStr.split("\\s+", 2)[0];
                String fieldValue = handleStr.split("\\s+", 2)[1];
                if (handle.equalsIgnoreCase(QueryConstants.FIELD_EQUAL) || handle.equalsIgnoreCase(QueryConstants.FIELD_EQ)) {
                    boolQueryBuilder.filter(!field.contains(QueryConstants.COMMA) ? QueryBuilders.termQuery(field, fieldValue)
                            : QueryBuilders.multiMatchQuery(fieldValue, Cu.convert2StrTrimArr(field.split(QueryConstants.COMMA))));
                } else if (handle.equalsIgnoreCase(QueryConstants.FIELD_NE)) {
                    boolQueryBuilder.mustNot(QueryBuilders.termQuery(field, fieldValue));
                } else if (handle.equalsIgnoreCase(QueryConstants.FIELD_LIKE)) {
                    String[] fields = Cu.convert2StrTrimArr(field.split(QueryConstants.COMMA));
                    Arrays.stream(fields).forEach(f -> boolQueryBuilder.filter(QueryBuilders.wildcardQuery(f, String.format("*%s*", fieldValue))));
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static SearchResult queryInScroll(Client client, SearchRequestBuilder requestBuilder, SearchResult searchResult, int maxIndexWindowResult) {
        int pageNum = (int) searchResult.getPageNum();
        int pageSize = (int) searchResult.getPageSize();
        Long cacheTime = 2000L;
        try {
            //第一次查询总量
            SearchResponse scrollResp = requestBuilder
                    .setScroll(new TimeValue(cacheTime))
                    .setSize(maxIndexWindowResult)
                    .get();
            //总共需要获取 (pageNum * pageSize)条。查询总量allNeedFetch超过maxIndexWindowResult条时，需要分批获取。
            int allNeedFetch = pageNum * pageSize;
            int scrollCount = getScrollCount(allNeedFetch, maxIndexWindowResult);
            for (int i = 0; i < scrollCount; i++) {
                if (scrollResp.getHits().getHits().length == 0) {
                    break;
                }
                scrollResp = client
                        .prepareSearchScroll(scrollResp.getScrollId())
                        .setScroll(new TimeValue(cacheTime))
                        .execute()
                        .actionGet();
            }
            //将查询结果转为Page的形式
            List<Map> records = new ArrayList<>();
            SearchHits hits = scrollResp.getHits();
            int begin = (pageNum - 1) * pageSize % maxIndexWindowResult;
            int end = (begin + pageSize) < hits.getHits().length ? (begin + pageSize) : hits.getHits().length;
            for (int i = begin; i < end; i++) {
                try {
                    SearchHit at = hits.getAt(i);
                    Map<String, Object> map = at.getSourceAsMap();
                    records.add(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            searchResult.setTotal(hits.totalHits);
            searchResult.setRecords(records);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EsSearchException("查询异常");
        }
        return searchResult;
    }

    private static int getScrollCount(int allNeedFetch, int eachFetch) {
        int count = 0;
        while (true) {
            if (count * eachFetch < allNeedFetch && (count + 1) * eachFetch >= allNeedFetch) {
                break;
            }
            count++;
        }
        return count;
    }

    public static void resolveHighLevelQuery(SearchSourceBuilder sourceBuilder,
                                             BoolQueryBuilder boolQueryBuilder,
                                             Map<String, Object> conditionMap,
                                             SearchResult searchResult) {
        for (Map.Entry<String, Object> entry : conditionMap.entrySet()) {
            String type = entry.getKey();
            Object value = entry.getValue();
            if (Cu.isNullOrEmpty(value)) {
                continue;
            }
            //根据查询条件分类组装。
            if (type.equalsIgnoreCase(QueryConstants.FIELD)) {
                resolveQueryField(boolQueryBuilder, value);
            } else if (type.equalsIgnoreCase(QueryConstants.HIGHTLIGHT)) {
                resolveQueryHightlight(boolQueryBuilder, value);
            } else if (type.equalsIgnoreCase(QueryConstants.RANGE)) {
                resolveHighLevelQueryRange(sourceBuilder, value);
            } else if (type.equalsIgnoreCase(QueryConstants.PAGE)) {
                resolveHighLevelQueryPage(sourceBuilder, value);
                setSearchResultPage(searchResult, value);
            } else if (type.equalsIgnoreCase(QueryConstants.SORT)) {
                resolveHighLevelQuerySort(sourceBuilder, value);
            } else if (type.equalsIgnoreCase(QueryConstants.FETCH) || type.equalsIgnoreCase(QueryConstants.FETCHSOURCE)) {
                resolveHighLevelQueryFetchSource(sourceBuilder, value);
            }
        }
    }

    private static void setSearchResultPage(SearchResult searchResult, Object value) {
        //处理分页 "page": "1,10"
        Optional.ofNullable(value).map(p -> p.toString()).ifPresent(e -> {
            int pageNum = Integer.valueOf(e.split(QueryConstants.COMMA)[0]);
            int pageSize = Integer.valueOf(e.split(QueryConstants.COMMA)[1]);
            searchResult.setPageNum(pageNum);
            searchResult.setPageSize(pageSize);
        });
    }

    private static void resolveHighLevelQueryFetchSource(SearchSourceBuilder sourceBuilder, Object value) {
        Object includeValue = ((Map) value).get(QueryConstants.FETCH_INCLUDES);
        Object excludeValue = ((Map) value).get(QueryConstants.FETCH_EXCLUDES);
        String[] includes = Cu.convert2StrTrimArr(includeValue);
        String[] excludes = Cu.convert2StrTrimArr(excludeValue);
        sourceBuilder.fetchSource(includes, excludes);
    }

    private static void resolveHighLevelQuerySort(SearchSourceBuilder sourceBuilder, Object value) {
        if (Cu.isNullOrEmpty(value) || !(value instanceof Map)) {
            return;
        }
        for (Map.Entry<String, String> entry : ((Map<String, String>) value).entrySet()) {
            sourceBuilder.sort(entry.getKey(), SortOrder.fromString(entry.getValue()));
        }
    }

    private static void resolveHighLevelQueryPage(SearchSourceBuilder sourceBuilder, Object value) {
        //处理分页 "page": "1,10"
        Optional.ofNullable(value).map(p -> p.toString()).ifPresent(e -> {
            int pageNum = Integer.valueOf(e.split(QueryConstants.COMMA)[0]);
            int pageSize = Integer.valueOf(e.split(QueryConstants.COMMA)[1]);
            sourceBuilder.from(pageNum - 1).size(pageSize);
        });
    }

    private static void resolveHighLevelQueryRange(SearchSourceBuilder sourceBuilder, Object value) {
        if (Cu.isNullOrEmpty(value) || !(value instanceof Map)) {
            return;
        }
        for (Map.Entry<String, String> entry : ((Map<String, String>) value).entrySet()) {
            String field = entry.getKey();
            String handleStr = entry.getValue().trim();
            String handle = handleStr.split("\\s+", 2)[0];
            String fieldValue = handleStr.split("\\s+", 2)[1];
            if (handle.equalsIgnoreCase(QueryConstants.BETWEEN)) {
                sourceBuilder.query(QueryBuilders.rangeQuery(field).from(fieldValue.split(QueryConstants.COMMA)[0]).to(fieldValue.split(QueryConstants.COMMA)[1]));
            } else if (handle.equalsIgnoreCase(QueryConstants.BETWEEN_FF)) {
                sourceBuilder.query(QueryBuilders.rangeQuery(field).from(fieldValue.split(QueryConstants.COMMA)[0], false).to(fieldValue.split(QueryConstants.COMMA)[1], false));
            } else if (handle.equalsIgnoreCase(QueryConstants.BETWEEN_FT)) {
                sourceBuilder.query(QueryBuilders.rangeQuery(field).from(fieldValue.split(QueryConstants.COMMA)[0], false).to(fieldValue.split(QueryConstants.COMMA)[1]));
            } else if (handle.equalsIgnoreCase(QueryConstants.BETWEEN_TF)) {
                sourceBuilder.query(QueryBuilders.rangeQuery(field).from(fieldValue.split(QueryConstants.COMMA)[0]).to(fieldValue.split(QueryConstants.COMMA)[1], false));
            } else if (handle.equalsIgnoreCase(QueryConstants.RANGE_LT)) {
                sourceBuilder.query(QueryBuilders.rangeQuery(field).lt(fieldValue));
            } else if (handle.equalsIgnoreCase(QueryConstants.RANGE_LTE)) {
                sourceBuilder.query(QueryBuilders.rangeQuery(field).lte(fieldValue));
            } else if (handle.equalsIgnoreCase(QueryConstants.RANGE_GT)) {
                sourceBuilder.query(QueryBuilders.rangeQuery(field).gt(fieldValue));
            } else if (handle.equalsIgnoreCase(QueryConstants.RANGE_GTE)) {
                sourceBuilder.query(QueryBuilders.rangeQuery(field).gte(fieldValue));
            }
        }
    }

    public static void convertSearchResonse2ResultList(SearchHits searchHits, SearchResult searchResult) {
        List resultList = new ArrayList();
        Arrays.stream(searchHits.getHits()).forEach(e -> resultList.add(e.getSourceAsMap()));
        searchResult.setTotal(searchHits.totalHits);
        searchResult.setRecords(resultList);
    }
}
