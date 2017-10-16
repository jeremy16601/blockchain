package com.blockchain.robot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
//public class RobotApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(RobotApplication.class, args);
//    }
//
//}
public class RobotApplication extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(RobotApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RobotApplication.class);
    }
}
