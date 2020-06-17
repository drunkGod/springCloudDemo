package com.jvxb.search.livable.service.impl;

import com.google.common.collect.Lists;
import com.jvxb.common.utils.BeanUtil;
import com.jvxb.search.livable.entity.Beauty;
import com.jvxb.search.livable.repository.BeautyRepository;
import com.jvxb.search.livable.service.BeautySearchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jvxb
 * @since 2020-06-11
 */
@Service
public class BeautySearchServiceImpl implements BeautySearchService {

    @Autowired
    private BeautyRepository beautyRepository;

    @Override
    public List queryList() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Iterable<Beauty> search = beautyRepository.search(boolQueryBuilder);
        List<Beauty> list = Lists.newArrayList(search);
//        List<Map> mapList = list.stream().map(BeanUtil::bean2Map).collect(Collectors.toList());
        return list;
    }
}
