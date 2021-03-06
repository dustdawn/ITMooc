package com.njit.course;

import com.njit.framework.interceptor.FeignClientInterceptor;
import org.mybatis.spring.annotation.MapperScan;
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
 * @date 2019/12/4 22:52
 */
@EnableFeignClients //开启feignClient
@EnableDiscoveryClient //EurekaClient能在EurekaServer被发现服务
@SpringBootApplication
@MapperScan("com.njit.course.com.njit.com.njit.course.com")
@EntityScan("com.njit.framework.domain.course")//扫描实体类
@ComponentScan(basePackages={"com.njit.api"})//扫描接口
@ComponentScan(basePackages={"com.njit.course"})
@ComponentScan(basePackages={"com.njit.framework"})//扫描common下的所有类
public class CourseApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(CourseApplication.class, args);
    }

    @Bean
    @LoadBalanced //开启客户端负载均衡
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

    /**远程接口调用feign拦截，需登录后才能调用*/
    @Bean
    public FeignClientInterceptor getFeignClientInterceptor() {
        return new FeignClientInterceptor();
    }
}
