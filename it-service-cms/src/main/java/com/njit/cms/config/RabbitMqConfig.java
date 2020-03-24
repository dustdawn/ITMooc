package com.njit.cms.config;

import com.njit.framework.client.RabbitMQList;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dustdawn
 * @date 2019/12/7 22:56
 */
@Configuration
public class RabbitMqConfig {
    /**
     * 交换机配置使用direct类型
     * @return
     */
    @Bean(RabbitMQList.EXCHANGES_CMS_POSTPAGE)
    public Exchange EXCHANGES_CMS_POSTPAGE() {
        return ExchangeBuilder.directExchange(RabbitMQList.EXCHANGES_CMS_POSTPAGE).durable(true).build();
    }
}
