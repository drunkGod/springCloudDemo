package com.jvxb.beauty;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication(scanBasePackages = "com.jvxb")
@EnableDiscoveryClient
@EnableCaching
@EnableRabbit
public class BeautyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeautyApplication.class, args);
    }

}
