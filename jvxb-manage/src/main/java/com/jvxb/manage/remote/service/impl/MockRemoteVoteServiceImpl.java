package com.jvxb.manage.remote.service.impl;

import com.jvxb.common.web.RespMsg;
import com.jvxb.manage.remote.service.RemoteVoteService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class MockRemoteVoteServiceImpl implements RemoteVoteService {

    @Override
    public RespMsg vote(HttpServletRequest request, Integer id) {
        System.out.println("投票失败咯，进入降级操作。投票id=" + id);
        return RespMsg.error("投票失败咯，进入降级操作。投票id=" + id);
    }

}
