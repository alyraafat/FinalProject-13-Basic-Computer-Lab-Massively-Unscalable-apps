package com.example.moderator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableFeignClients(basePackages = "com.example.moderator.clients")
public class ModeratorApplication {
	public static void main(String[] args) {
		SpringApplication.run(ModeratorApplication.class, args);
	}
}
