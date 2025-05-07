package com.example.reddit.ThreadsService.dto;

import java.util.UUID;

public class DeleteCommentRequest {
    private UUID commentId;
    private UUID threadId;
    private UUID moderatorId;

    public DeleteCommentRequest() {}
    public DeleteCommentRequest(UUID commentId, UUID threadId, UUID moderatorId) {
        this.commentId = commentId;
        this.threadId = threadId;
        this.moderatorId = moderatorId;
    }

    public UUID getCommentId() {
        return commentId;
    }

    public UUID getThreadId() {
        return threadId;
    }

    public UUID getModeratorId() {
        return moderatorId;
    }

    public void setCommentId(UUID commentId) {
        this.commentId = commentId;
    }

    public void setThreadId(UUID threadId) {
        this.threadId = threadId;
    }

    public void setModeratorId(UUID moderatorId) {
        this.moderatorId = moderatorId;
    }
}
