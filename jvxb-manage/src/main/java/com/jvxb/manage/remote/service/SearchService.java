package com.jvxb.manage.remote.service;

import com.jvxb.common.base.entity.es.EsDocument;
import com.jvxb.manage.remote.service.impl.MockSearchServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 搜索服务
 * @author jvxb
 * @since 2020-06-13
 */
@FeignClient(value = "jvxb-search", fallback = MockSearchServiceImpl.class)
public interface SearchService {

    @PostMapping("/search/beauty/v1/save")
    void save(@RequestBody EsDocument esDocument);

    @PostMapping("/search/beauty/v1/saveAll")
    void saveAll(@RequestBody EsDocument esDocument);

    @PostMapping("/search/beauty/v1/delete")
    void delete(@RequestBody EsDocument esDocument);
}
