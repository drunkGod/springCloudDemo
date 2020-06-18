package com.jvxb.beauty.remote.impl;

import com.google.common.collect.Lists;
import com.jvxb.beauty.livable.entity.Beauty;
import com.jvxb.beauty.remote.SearchService;
import com.jvxb.common.base.entity.es.EsDocument;
import com.jvxb.common.web.RespMsg;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jvxb
 * @since 2020-06-13
 */
@Log4j2
@Component
public class MockSearchServiceImpl implements SearchService {
    @Override
    public void save(EsDocument esDocument) {
        log.error("存储es失败：" + esDocument);
    }

    @Override
    public RespMsg<List<Beauty>> list(String name) {
        log.error("查询es失败：" + name);
        return null;
    }
}
