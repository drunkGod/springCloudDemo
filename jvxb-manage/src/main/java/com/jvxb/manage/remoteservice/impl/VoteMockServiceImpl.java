package com.jvxb.manage.remoteservice.impl;


import com.jvxb.modules.livable.service.remoteservice.VoteService;

import java.util.Map;

public class VoteMockServiceImpl implements VoteService {

    @Override
    public void vote(Map<String, Object> voteMap) {
        System.out.println(voteMap);
        System.out.println("投票失败咯，进入降级操作。");
    }
}
