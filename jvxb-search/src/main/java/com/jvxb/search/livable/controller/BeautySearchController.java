package com.jvxb.search.livable.controller;

import com.alibaba.fastjson.JSON;
import com.jvxb.common.utils.BeanUtil;
import com.jvxb.common.web.RespMsg;
import com.jvxb.search.livable.entity.Beauty;
import com.jvxb.search.livable.entity.EsDocument;
import com.jvxb.search.livable.repository.BeautyRepository;
import com.jvxb.search.livable.service.BeautySearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jvxb
 * @since 2020-04-05
 */
@RestController
@RequestMapping("/search/beauty/v1")
public class BeautySearchController {

    @Autowired
    private BeautySearchService beautySearchService;
    @Autowired
    private BeautyRepository beautyRepository;

    @RequestMapping("list")
    public RespMsg list() {
        System.out.println("搜索全部美女");
        List<Map> mapList = beautySearchService.queryList();
        return RespMsg.ok(mapList);
    }

    @PostMapping("save/all")
    public Object saveAll(List<EsDocument> esDocumentList) {
        List<Beauty> beauties = new ArrayList<>();
        for (EsDocument esDocument : esDocumentList) {
            beauties.add(BeanUtil.map2Bean(esDocument.getDocument(), Beauty.class));
        }
        beautyRepository.saveAll(beauties);
        return RespMsg.ok();
    }

    @PostMapping("save")
    public Object save(EsDocument document) {
        Beauty beauty = BeanUtil.map2Bean(document.getDocument(), Beauty.class);
        beautyRepository.save(beauty);
        return RespMsg.ok();
    }

    @PostMapping("delete")
    public Object delete(EsDocument document) {
        Beauty beauty = BeanUtil.map2Bean(document.getDocument(), Beauty.class);
        beautyRepository.delete(beauty);
        return RespMsg.ok();
    }


}

