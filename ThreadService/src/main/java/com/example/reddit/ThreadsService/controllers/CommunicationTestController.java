// FOR TESTING PURPOSES, DON'T REMOVE

package com.example.reddit.ThreadsService.controllers;

import com.example.reddit.ThreadsService.rabbitmq.ThreadProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/test/thread")
public class CommunicationTestController {
    private final ThreadProducer threadProducer;

    @Autowired
    public CommunicationTestController(ThreadProducer threadProducer) {
        this.threadProducer = threadProducer;
    }

    @GetMapping
    public void testThreadProducer() {
        UUID threadId = UUID.randomUUID();
        threadProducer.sendReportRequest(threadId, "test content");
    }
}
