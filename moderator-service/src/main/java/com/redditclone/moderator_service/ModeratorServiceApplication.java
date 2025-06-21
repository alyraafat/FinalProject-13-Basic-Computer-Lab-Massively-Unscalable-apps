package com.redditclone.moderator_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableFeignClients(basePackages = "com.example.moderator.clients")
public class ModeratorServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModeratorServiceApplication.class, args);
    }
}
