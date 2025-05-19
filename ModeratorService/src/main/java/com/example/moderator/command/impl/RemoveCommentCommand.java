package com.example.moderator.command.impl;

import com.example.moderator.command.Command;
import com.example.moderator.rabbitmq.ModeratorProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

public class RemoveCommentCommand implements Command<Void> {

    private final UUID commentId;
    private final UUID threadId;
    private final ModeratorProducer moderatorProducer;

    public RemoveCommentCommand(UUID commentId, UUID threadId, ModeratorProducer moderatorProducer) {
        this.commentId = commentId;
        this.moderatorProducer = moderatorProducer;
        this.threadId = threadId;
    }

    @Override
    public Void execute() {
        // Perform the comment deletion logic here
        System.out.println("Comment deletion logic executed for comment ID: " + commentId);

        try {
            // Send delete request asynchronously via RabbitMQ
            moderatorProducer.sendDeleteCommentRequest(commentId, threadId);
        } catch (Exception e) {
            throw new CommentDeletionFailedException(commentId, e);
        }
        return null;
    }

    public static class CommentDeletionFailedException extends RuntimeException {
        public CommentDeletionFailedException(UUID commentId, Throwable cause) {
            super("Failed to delete comment " + commentId + ": " + cause.getMessage(), cause);
        }
    }
}