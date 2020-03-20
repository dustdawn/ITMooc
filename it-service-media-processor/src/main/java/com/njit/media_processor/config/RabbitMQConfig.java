package com.njit.media_processor.config;

import com.njit.framework.client.RabbitMQList;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dustdawn
 * @date 2020/3/5 22:08
 */
@Configuration
public class RabbitMQConfig {


    /**
     * 视频处理路由key
     */
    @Value("${it-service-media.mq.routingkey-media-video}")
    public  String routingkey_media_video;

    //消费者并发数量
    public static final int DEFAULT_CONCURRENT = 10;

    /**
     * 配置mq的容器工厂参数，增加并发处理数量即可实现多线程处理监听队列，实现多线程处理消息
     * @param configurer
     * @param connectionFactory
     * @return
     */
    @Bean("customContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        factory.setMaxConcurrentConsumers(DEFAULT_CONCURRENT);
        configurer.configure(factory, connectionFactory);
        return factory;
    }


    /**
     * 交换机配置
     * @return the exchange
     */
    @Bean(RabbitMQList.EXCHANGES_MEDIA_PROCESSOR)
    public Exchange EXCHANGES_MEDIA_PROCESSOR() {
        return ExchangeBuilder.directExchange(RabbitMQList.EXCHANGES_MEDIA_PROCESSOR).durable(true).build();
    }
    //声明队列
    @Bean(RabbitMQList.QUEUE_MEDIA_PROCESSOR)
    public Queue QUEUE_MEDIA_PROCESSOR() {
        Queue queue = new Queue(RabbitMQList.QUEUE_MEDIA_PROCESSOR,true,false,true);
        return queue;
    }
    /**
     * 绑定队列到交换机 .
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding binding_queue_media_processtask(@Qualifier(RabbitMQList.QUEUE_MEDIA_PROCESSOR) Queue queue,
                                                   @Qualifier(RabbitMQList.EXCHANGES_MEDIA_PROCESSOR) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey_media_video).noargs();
    }
}
