package com.jvxb.manage.remoteservice;

import com.jvxb.modules.livable.service.remoteservice.VoteService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author lcl
 * @since 2020-04-11
 */

@FeignClient(value = "jvxb-beauty")
public interface RemoteVoteService {

    @GetMapping("/beauty/vote")
    void vote(@RequestParam("request") HttpServletRequest request, @RequestParam("id") Integer id);
}
