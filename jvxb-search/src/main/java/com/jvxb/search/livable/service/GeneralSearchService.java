package com.jvxb.search.livable.service;

import com.jvxb.search.livable.entity.EsDocument;
import com.jvxb.search.livable.entity.SearchResult;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author jvxb
 * @since 2020-06-16
 */
public interface GeneralSearchService {

    void saveDoc(EsDocument esDocument);

    void batchSaveDoc(List<EsDocument> documentList);

    void deleteDoc(@RequestBody EsDocument esDocument);

    SearchResult query(String index, String type, String condition);

    SearchResult queryByClient(String node, String index, String type, String condition);

    Map queryById(String index, String type, String id);

}
