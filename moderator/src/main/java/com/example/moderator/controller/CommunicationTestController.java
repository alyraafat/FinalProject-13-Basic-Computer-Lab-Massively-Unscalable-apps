// FOR TESTING PURPOSES ONLY

package com.example.moderator.controller;

import com.example.moderator.dto.BanRequest;
import com.example.moderator.dto.UnbanRequest;
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

    @GetMapping("/ban")
    public void testBanProducer() {
        UUID commentId = UUID.randomUUID();
        UUID userID = UUID.randomUUID();
        BanRequest banRequest = new BanRequest(userID, commentId);
        moderatorProducer.sendBanMemberRequest(banRequest);
    }

    @GetMapping("/unban")
    public void testUnBanProducer() {
        UUID commentId = UUID.randomUUID();
        UUID userID = UUID.randomUUID();
        UnbanRequest unbanRequest = new UnbanRequest(userID, commentId);
        moderatorProducer.sendUnBanMemberRequest(unbanRequest);
    }
}
