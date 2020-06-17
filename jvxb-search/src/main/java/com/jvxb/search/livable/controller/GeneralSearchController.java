package com.jvxb.search.livable.controller;

import com.jvxb.common.web.RespMsg;
import com.jvxb.search.livable.entity.EsDocument;
import com.jvxb.search.livable.entity.SearchResult;
import com.jvxb.search.livable.service.GeneralSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 通用搜索服务：增删改查
 *
 * @author jvxb
 * @since 2020-06-13
 */
@RestController
@RequestMapping("/search/general/v1")
public class GeneralSearchController {

    @Autowired
    private GeneralSearchService generalSearchService;

    /**
     * 保存或更新Es文档
     *
     * @param esDocument
     * @return
     */
    @PostMapping("saveDoc")
    public <T> void saveDoc(@RequestBody EsDocument esDocument) {
        generalSearchService.saveDoc(esDocument);
    }

    /**
     * 批量保存或更新Es文档
     *
     * @param documentList
     * @return
     */
    @PostMapping("batchSaveDoc")
    public void batchSaveDoc(@RequestBody List<EsDocument> documentList) {
        generalSearchService.batchSaveDoc(documentList);
    }

    /**
     * 删除Es文档
     *
     * @param esDocument
     */
    @PostMapping("deleteDoc")
    public void deleteDoc(@RequestBody EsDocument esDocument) {
        generalSearchService.deleteDoc(esDocument);
    }

    /**
     * 通过条件查询document。结果分页。
     *
     * @param index
     * @param type
     * @param condition 查询条件MapStr中的key有:
     * - 字段（field），对应内容有：equal（等于）、ne（不等于）、like（近似）等，
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
     * "limit": "1,10"
     * "fetch": "id,name"，或者{"includes": ["id","name"], "excludes": null}、{"includes": null, "excludes": ["id","name"]}
     * }
     * @return
     */
    @GetMapping("query")
    public RespMsg<SearchResult> query(@RequestParam("index") String index,
                                       @RequestParam("type") String type,
                                       String condition) {
        SearchResult searchResult =  generalSearchService.query(index, type, condition);
        return RespMsg.ok(searchResult);
    }

    /**
     * 通过主键查询document
     *
     * @param index
     * @param type
     * @param id
     * @return
     */
    @GetMapping("queryById")
    public RespMsg<Map> queryById(@RequestParam("index") String index, @RequestParam("type") String type, @RequestParam("id") String id) {
        Map resultMap = generalSearchService.queryById(index, type,id);
        return RespMsg.ok(resultMap);
    }

    /**
     * 通过条件和客户端查询document。结果分页。
     *
     * @param index
     * @param type
     * @param node
     * @param condition
     * @return
     */
    @GetMapping("queryByNode")
    public RespMsg<SearchResult> queryByClient(@RequestParam("index") String index,
                                               @RequestParam("type") String type,
                                               @RequestParam("node") String node,
                                               String condition) {

        SearchResult searchResult = generalSearchService.queryByClient(node, index, type, condition);
        return RespMsg.ok(searchResult);
    }



}
