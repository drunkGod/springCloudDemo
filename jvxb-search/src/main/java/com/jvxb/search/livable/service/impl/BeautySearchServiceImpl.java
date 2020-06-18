package com.jvxb.search.livable.service.impl;

import com.google.common.collect.Lists;
import com.jvxb.common.utils.BeanUtil;
import com.jvxb.common.utils.Cu;
import com.jvxb.search.livable.entity.Beauty;
import com.jvxb.search.livable.repository.BeautyRepository;
import com.jvxb.search.livable.service.BeautySearchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
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
    public List queryList(String name) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(Cu.notNullOrEmpty(name)) {
            boolQueryBuilder.filter(QueryBuilders.matchPhraseQuery(Beauty.NAME, name));
        }
        Iterable<Beauty> search = beautyRepository.search(boolQueryBuilder);
        List<Beauty> list = Lists.newArrayList(search);
        return list;
    }
}
