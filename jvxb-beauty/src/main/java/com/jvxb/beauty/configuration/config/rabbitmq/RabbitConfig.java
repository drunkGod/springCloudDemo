package com.jvxb.beauty.configuration.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    //投票队列：DirectQueue
    @Bean
    public Queue VoteDirectQueue() {
        return new Queue(MqConstant.VOTE_QUENE,true);  //true 是否持久
    }

    //投票直连交换机：DirectExchange
    @Bean
    DirectExchange VoteDirectExchange() {
        return new DirectExchange(MqConstant.VOTE_DIRECT_EXCHANGE);
    }

    //投票绑定：将队列和交换机绑定, 并设置用于匹配键：投票路由键DirectRouting
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(VoteDirectQueue()).to(VoteDirectExchange()).with(MqConstant.VOTE_DIRECT_ROUTING);
    }

    /**消息的转换器
     * 设置成json 并放入到Spring中
     * */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}