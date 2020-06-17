package com.jvxb.manage.remote.service;

import com.jvxb.common.web.RespMsg;
import com.jvxb.manage.remote.service.impl.MockRemoteVoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 通过网关，调用jvxb-beauty服务
 * @author lcl
 * @since 2020-04-11
 */
@FeignClient(value = "jvxb-beauty", fallback = MockRemoteVoteServiceImpl.class)
public interface RemoteVoteService {

    @GetMapping("/beauty/vote")
    RespMsg vote(@RequestParam("request") HttpServletRequest request, @RequestParam("id") Integer id);
}
