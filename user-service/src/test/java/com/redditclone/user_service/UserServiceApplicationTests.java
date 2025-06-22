package com.redditclone.user_service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.RedisServer;

import java.io.IOException;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test")
@ActiveProfiles("test")
public class UserServiceApplicationTests {

    private static RedisServer redisServer;

    @BeforeAll
    static void setUpRedis() throws IOException {
        // Start an embedded Redis server before all tests
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        // Stop the embedded Redis server after all tests
        redisServer.stop();
    }
}
