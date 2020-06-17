package com.jvxb.beauty.remote;

import com.jvxb.beauty.livable.entity.Beauty;
import com.jvxb.beauty.remote.impl.MockSearchServiceImpl;
import com.jvxb.common.base.entity.es.EsDocument;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 搜索服务
 * @author jvxb
 * @since 2020-06-13
 */
@FeignClient(value = "jvxb-search", fallback = MockSearchServiceImpl.class)
public interface SearchService {

    @PostMapping("/search/beauty/v1/save")
    void save(@RequestBody EsDocument esDocument);

    @GetMapping("/search/beauty/v1/list")
    List<Beauty> list(@RequestParam("name") String name);
}
