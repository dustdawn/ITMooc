package com.njit.media_processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author dustdawn
 * @date 2020/3/20 11:53
 */
@SpringBootApplication
@EntityScan("com.njit.framework.domain.media")//扫描实体类
@ComponentScan(basePackages={"com.njit.api"})//扫描接口
@ComponentScan(basePackages={"com.njit.media_processor"})//扫描本项目下的所有类
@ComponentScan(basePackages={"com.njit.framework"})//扫描common下的所有类
public class MediaProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediaProcessorApplication.class, args);
    }
}
