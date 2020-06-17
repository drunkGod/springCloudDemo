package com.jvxb.beauty.configuration.rabbitmq.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.Condition;
import com.jvxb.beauty.livable.entity.Beauty;
import com.jvxb.beauty.livable.service.BeautyService;
import com.jvxb.beauty.remote.SearchService;
import com.jvxb.common.base.entity.es.EsDocument;
import com.jvxb.common.utils.BeanUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VoteReceiver {

    @Autowired
    private BeautyService beautyService;
    @Autowired
    private SearchService searchService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.VOTE_QUENE)//监听的队列名称 voteDirectQueue
    public void process(Map voteMap, Channel channel, Message message) {
        System.out.println("-----------------------");
        System.out.println("voteReceiver消费者收到消息  : " + voteMap.toString());
        try {
            synchronized (this) {
                //修改数据库
                Beauty beauty = beautyService.getOne(Condition.<Beauty>create().select(Beauty.PS, Beauty.ID)
                        .eq(Beauty.ID, voteMap.get("voteId")));
                beauty.setPs(beauty.getPs() + 1);
                beautyService.updateById(beauty);
                //同时更新es
                searchService.save(new EsDocument("beauty_index", "beauty", BeanUtil.bean2Map(beauty)));
                //在acknowledge-mode: manual 时，需手动确认，才可以在队列删掉
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            }
        } catch (Exception e) {
            System.out.println("更新数据库票数失败：”");
            System.out.println(e.getMessage());
        }
        System.out.println("-----------------------");
    }

}