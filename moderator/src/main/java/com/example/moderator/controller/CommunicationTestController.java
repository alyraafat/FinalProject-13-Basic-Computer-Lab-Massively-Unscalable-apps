// FOR TESTING PURPOSES ONLY

package com.example.moderator.controller;

import com.example.moderator.rabbitmq.ModeratorProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/test/moderator")
public class CommunicationTestController {
    private final ModeratorProducer moderatorProducer;

    @Autowired
    public CommunicationTestController(ModeratorProducer moderatorProducer) {
        this.moderatorProducer = moderatorProducer;
    }

    @GetMapping
    public void testModeratorProducer() {
        UUID commentId = UUID.randomUUID();

        moderatorProducer.sendDeleteCommentRequest(commentId);
    }
}
