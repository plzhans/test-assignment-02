package com.plzhans.assignment.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods=false)
public class APIServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(APIServerApplication.class, args);
    }
}