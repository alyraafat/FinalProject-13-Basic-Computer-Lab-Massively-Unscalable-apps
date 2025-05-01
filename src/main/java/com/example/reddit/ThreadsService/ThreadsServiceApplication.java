package com.example.reddit.ThreadsService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ThreadsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThreadsServiceApplication.class, args);
	}

}
