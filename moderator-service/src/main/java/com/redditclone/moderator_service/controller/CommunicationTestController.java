// FOR TESTING PURPOSES ONLY

package com.redditclone.moderator_service.controller;

import com.redditclone.moderator_service.dto.BanRequest;
import com.redditclone.moderator_service.dto.UnbanRequest;
import com.redditclone.moderator_service.rabbitmq.ModeratorProducer;
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
        UUID threadId = UUID.randomUUID();
        moderatorProducer.sendDeleteCommentRequest(commentId, threadId);
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
