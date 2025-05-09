package com.example.reddit.ThreadsService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableCaching
@EnableFeignClients
public class ThreadsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThreadsServiceApplication.class, args);
	}

}
