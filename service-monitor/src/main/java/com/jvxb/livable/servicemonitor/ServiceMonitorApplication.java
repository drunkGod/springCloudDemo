package com.jvxb.livable.servicemonitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
public class ServiceMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceMonitorApplication.class, args);
    }

}
