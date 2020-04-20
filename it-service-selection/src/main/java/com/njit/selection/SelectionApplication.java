package com.njit.selection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author dustdawn
 * @date 2020/4/19 15:09
 */
@EnableDiscoveryClient
@EnableFeignClients
@EntityScan(value={"com.njit.framework.domain.order","com.njit.framework.domain.task"})//扫描实体类
@ComponentScan(basePackages={"com.njit.api"})//扫描接口
@ComponentScan(basePackages={"com.njit.framework"})//扫描framework中通用类
@ComponentScan(basePackages={"com.njit.selection"})//扫描本项目下的所有类
@SpringBootApplication
public class SelectionApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SelectionApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}