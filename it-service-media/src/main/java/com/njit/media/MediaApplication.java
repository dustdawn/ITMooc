package com.njit.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author dustdawn
 * @date 2020/3/20 11:22
 */
@EnableDiscoveryClient//从Eureka Server获取服务
@SpringBootApplication//扫描所在包及子包的bean，注入到ioc中
@EntityScan("com.njit.framework.domain.media")//扫描实体类
@ComponentScan(basePackages={"com.njit.api"})//扫描接口
@ComponentScan(basePackages={"com.njit.framework"})//扫描framework中通用类
@ComponentScan(basePackages={"com.njit.media"})//扫描本项目下的所有类
public class MediaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediaApplication.class,args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
