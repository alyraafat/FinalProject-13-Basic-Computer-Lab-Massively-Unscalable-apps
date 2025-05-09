package com.example.moderator.command.impl;

import com.example.moderator.command.Command;
import com.example.moderator.dto.BanRequest;
import com.example.moderator.rabbitmq.ModeratorProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

public class BanUserCommand implements Command<Void> {

    private UUID userID;
    private UUID communityID;
    private ModeratorProducer moderatorProducer;


    public BanUserCommand () {
    }

    public BanUserCommand (UUID userID, UUID communityID, ModeratorProducer moderatorProducer) {
        this.userID = userID;
        this.communityID = communityID;
        this.moderatorProducer = moderatorProducer;
    }

    @Override
    public Void execute() {
        System.out.println("Ban User");
        BanRequest banRequest = new BanRequest(userID, communityID);
        moderatorProducer.sendBanMemberRequest(banRequest);
        return null;
    }

//    public static class CommentDeletionFailedException extends RuntimeException {
//        public CommentDeletionFailedException(UUID commentId, Throwable cause) {
//            super("Failed to delete comment " + commentId + ": " + cause.getMessage(), cause);
//        }
//    }
}
