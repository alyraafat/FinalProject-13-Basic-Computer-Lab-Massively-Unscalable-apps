package com.example.moderator.command.impl;

import com.example.moderator.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

public class RemoveCommentCommand implements Command<Void> {

    private UUID commentId;


    public RemoveCommentCommand (UUID commentId) {
        this.commentId = commentId;
    }

    public RemoveCommentCommand() {
    }

    @Override
    public Void execute() {
        System.out.println("comment deleted");
        return null;
    }

    public static class CommentDeletionFailedException extends RuntimeException {
        public CommentDeletionFailedException(UUID commentId, Throwable cause) {
            super("Failed to delete comment " + commentId + ": " + cause.getMessage(), cause);
        }
    }
}