package com.redditclone.thread_service.controllers;


import com.redditclone.thread_service.models.Thread;
import com.redditclone.thread_service.rabbitmq.ThreadProducer;
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
