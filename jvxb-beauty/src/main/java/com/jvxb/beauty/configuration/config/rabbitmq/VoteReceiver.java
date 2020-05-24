package com.jvxb.beauty.configuration.config.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.Condition;
import com.jvxb.beauty.livable.entity.Beauty;
import com.jvxb.beauty.livable.service.BeautyService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Lazy(false)
public class VoteReceiver {

    @Autowired
    private BeautyService beautyService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.VOTE_QUENE)//监听的队列名称 voteDirectQueue
    public void process(Map voteMap, Channel channel, Message message) {
        System.out.println("-----------------------");
        System.out.println("voteReceiver消费者收到消息  : " + voteMap.toString());
        try {
            synchronized (this) {
                Beauty beauty = beautyService.getOne(Condition.<Beauty>create().select(Beauty.PS, Beauty.ID)
                        .eq(Beauty.ID, voteMap.get("voteId")));
                beauty.setPs(beauty.getPs() + 1);
                beautyService.updateById(beauty);
                //在acknowledge-mode: manual 时，需手动确认，才可以在队列删掉
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("-----------------------");
    }

}