package com.example.moderator.command.impl;

import com.example.moderator.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

public class BanUserCommand implements Command<Void> {

    private UUID userID;
    private UUID communityID;


    public BanUserCommand () {
    }

    public BanUserCommand (UUID userID, UUID communityID) {
        this.userID = userID;
        this.communityID = communityID;
    }


    @Override
    public Void execute() {
        System.out.println("Ban User");
        return null;
    }

//    public static class CommentDeletionFailedException extends RuntimeException {
//        public CommentDeletionFailedException(UUID commentId, Throwable cause) {
//            super("Failed to delete comment " + commentId + ": " + cause.getMessage(), cause);
//        }
//    }
}
