package com.bkcc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 【描 述】：简要日志分析模块启动类
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-20 14:17
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableEurekaClient
@EnableAsync
@EnableScheduling
@MapperScan("com.bkcc.logans.mapper")
public class LogansApplication extends SpringBootServletInitializer {

    /**
     * 【描 述】：启动方法
     *
     * @param args
     * @return void
     * @author 陈汝晗
     * @since 2019/9/20 14:21
     */
    public static void main(String[] args) {
        SpringApplication.run(LogansApplication.class, args);
    }

}///:~



