package com.jvxb.beauty.livable.service.impl;

import com.jvxb.beauty.configuration.config.rabbitmq.VoteSender;
import com.jvxb.modules.livable.service.remoteservice.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("voteService")
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteSender voteSender;

    @Override
    public void vote(Map<String, Object> voteMap) {
        voteSender.convertAndSend(voteMap);
    }
}
