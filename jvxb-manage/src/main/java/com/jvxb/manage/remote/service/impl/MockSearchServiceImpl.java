package com.jvxb.manage.remote.service.impl;

import com.jvxb.common.base.entity.es.EsDocument;
import com.jvxb.manage.remote.service.SearchService;
import lombok.extern.log4j.Log4j2;

/**
 * @author jvxb
 * @since 2020-06-13
 */
@Log4j2
public class MockSearchServiceImpl implements SearchService {
    @Override
    public void save(EsDocument esDocument) {
        log.error("存储es失败：" + esDocument);
    }

    @Override
    public void saveAll(EsDocument esDocument) {
        log.error("存储es失败：" + esDocument);
    }

    @Override
    public void delete(EsDocument esDocument) {
        log.error("删除es失败：" + esDocument);
    }
}
