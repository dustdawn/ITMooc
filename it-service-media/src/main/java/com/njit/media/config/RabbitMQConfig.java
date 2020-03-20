package com.njit.media.config;

import com.njit.framework.client.RabbitMQList;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dustdawn
 * @date 2020/3/20 11:34
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
     * 交换机配置
     * @return the exchange
     */
    @Bean(RabbitMQList.EXCHANGES_MEDIA_PROCESSOR)
    public Exchange EXCHANGES_MEDIA_PROCESSOR() {
        return ExchangeBuilder.directExchange(RabbitMQList.EXCHANGES_MEDIA_PROCESSOR).durable(true).build();
    }


}
