package com.njit.cms;

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
 * @date 2020/3/19 18:55
 */
@EnableDiscoveryClient //EurekaClient能在EurekaServer被发现服务
@SpringBootApplication
@EntityScan("com.njit.framework.domain.cms")//扫描实体类
@ComponentScan(basePackages = {"com.njit.api"})//扫描接口
@ComponentScan(basePackages = {"com.njit.framework"})//扫描common包下类
@ComponentScan(basePackages = {"com.njit.cms"})//扫描本项目下所有类
public class CmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsApplication.class);
    }

    /**
     * SpringMVC提供 RestTemplate请求http接口，RestTemplate的底层可以使用第三方的http客户端工具实现http 的
     * 请求，常用的http客户端工具有Apache HttpClient、OkHttpClient等，本项目使用OkHttpClient完成http请求，
     * 原因也是因为它的性能比较出众。
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
