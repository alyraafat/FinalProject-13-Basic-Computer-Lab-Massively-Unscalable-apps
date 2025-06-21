// FOR TESTING PURPOSES, DON'T REMOVE

package com.redditclone.thread_service.controllers;

import com.redditclone.thread_service.rabbitmq.ThreadProducer;
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
        UUID userReporting = UUID.randomUUID();
        UUID itemReported = UUID.randomUUID();
        String reportDescription = "test report";
        UUID communityId = UUID.randomUUID();

        threadProducer.sendReportRequest(userReporting, itemReported, reportDescription, communityId);
    }
}
