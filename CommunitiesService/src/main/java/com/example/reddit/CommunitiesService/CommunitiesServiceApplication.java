package com.example.reddit.CommunitiesService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.reddit.CommunitiesService.clients")
public class CommunitiesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunitiesServiceApplication.class, args);
	}

}
