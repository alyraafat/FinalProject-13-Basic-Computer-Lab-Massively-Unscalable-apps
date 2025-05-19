package com.example.reddit.ThreadsService.dto;

import java.util.UUID;

public class DeleteCommentRequest {
    private UUID commentId;
    private UUID threadId;
    public DeleteCommentRequest() {}

    public DeleteCommentRequest(UUID commentId, UUID threadId) {
        this.commentId = commentId;
        this.threadId = threadId;
    }

    public UUID getCommentId() {
        return commentId;
    }

    public void setCommentId(UUID commentId) {
        this.commentId = commentId;
    }

    public UUID getThreadId() {
        return threadId;
    }

    public void setThreadId(UUID threadId) {
        this.threadId = threadId;
    }
}
