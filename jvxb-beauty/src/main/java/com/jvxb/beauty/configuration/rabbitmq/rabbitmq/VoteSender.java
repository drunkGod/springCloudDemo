package com.jvxb.beauty.configuration.rabbitmq.rabbitmq;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VoteSender implements RabbitTemplate.ConfirmCallback {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void convertAndSend(Map voteMap) {
        rabbitTemplate.setConfirmCallback(this);
        CorrelationData correlationData = new CorrelationData(voteMap.toString());
        rabbitTemplate.convertAndSend(MqConstant.VOTE_DIRECT_EXCHANGE, MqConstant.VOTE_DIRECT_ROUTING, voteMap, correlationData);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean isSendSuccess, String error) {
        System.out.println(String.format("开始发送到MQ【%s】：发送内容: 【%s】", MqConstant.VOTE_DIRECT_EXCHANGE, correlationData.getId()));
        if (isSendSuccess) {
            System.out.println(String.format("【发送成功】到MQ【%s】：发送内容: 【%s】", MqConstant.VOTE_DIRECT_EXCHANGE, correlationData.getId()));
        } else {
            System.out.println(String.format("【发送失败】到MQ【%s】：发送内容: 【%s】", MqConstant.VOTE_DIRECT_EXCHANGE, correlationData.getId()));
        }

    }
}