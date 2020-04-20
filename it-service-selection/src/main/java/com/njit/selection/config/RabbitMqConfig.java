package com.njit.selection.config;

import com.njit.framework.client.RabbitMQList;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dustdawn
 * @date 2020/4/19 15:22
 */
@Configuration
public class RabbitMqConfig {

    /**
     * 添加选课路由key
     */
    public static final String ROUTINGKEY_LEARNING_ADDCHOOSECOURSE = "addchoosecourse";
    /**
     * 完成添加选课路由key
     */
    public static final String ROUTINGKEY_LEARNING_FINISHADDCHOOSECOURSE = "finishaddchoosecourse";

    /**
     * 交换机配置
     * @return the exchange
     */
    @Bean(RabbitMQList.EXCHANGES_LEARNING_ADDCHOOSECOURSE)
    public Exchange EX_DECLARE() {
        return ExchangeBuilder.directExchange(RabbitMQList.EXCHANGES_LEARNING_ADDCHOOSECOURSE).durable(true).build();
    }

    //声明队列 添加选课队列
    @Bean(RabbitMQList.QUEUE_LEARNING_ADDCHOOSECOURSE)
    public Queue QUEUE_DECLARE_add() {
        Queue queue = new Queue(RabbitMQList.QUEUE_LEARNING_ADDCHOOSECOURSE,true,false,true);
        return queue;
    }

    //声明队列 完成添加选课队列
    @Bean(RabbitMQList.QUEUE_LEARNING_FINISHADDCHOOSECOURSE)
    public Queue QUEUE_DECLARE() {
        Queue queue = new Queue(RabbitMQList.QUEUE_LEARNING_FINISHADDCHOOSECOURSE,true,false,true);
        return queue;
    }

    /**
     * 绑定 添加选课队列到交换机 .
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding binding_queue_addchoosecourse_processtask(@Qualifier(RabbitMQList.QUEUE_LEARNING_ADDCHOOSECOURSE) Queue queue, @Qualifier(RabbitMQList.EXCHANGES_LEARNING_ADDCHOOSECOURSE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_LEARNING_ADDCHOOSECOURSE).noargs();
    }

    /**
     * 绑定 完成选课 队列到交换机 .
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding binding_queue_finishaddchoosecourse_processtask(@Qualifier(RabbitMQList.QUEUE_LEARNING_FINISHADDCHOOSECOURSE) Queue queue, @Qualifier(RabbitMQList.EXCHANGES_LEARNING_ADDCHOOSECOURSE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_LEARNING_FINISHADDCHOOSECOURSE).noargs();
    }

}

