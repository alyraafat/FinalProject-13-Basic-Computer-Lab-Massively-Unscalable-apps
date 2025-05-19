package com.example.reddit.ThreadsService.controllers;


import com.example.reddit.ThreadsService.models.Thread;
import com.example.reddit.ThreadsService.rabbitmq.ThreadProducer;
import com.example.reddit.ThreadsService.services.ThreadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/tests")
public class TestContoller {
    private final ThreadProducer threadProducer;

    public TestContoller(ThreadProducer threadProducer) {
        this.threadProducer = threadProducer;
    }

    @GetMapping
    public void getAllThreads() {
        UUID uuid = UUID.randomUUID();
        Thread thread = new Thread.Builder()
                .id(UUID.randomUUID()).title("Hellooooo").build();
        threadProducer.sendThreadNotificationRequest(thread, "comment");
        threadProducer.sendThreadNotificationRequest(thread, "up");
        threadProducer.sendThreadNotificationRequest(thread, "down");
    }
}
