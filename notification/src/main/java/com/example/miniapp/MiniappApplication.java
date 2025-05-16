package com.example.miniapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MiniappApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniappApplication.class, args);
	}

}
