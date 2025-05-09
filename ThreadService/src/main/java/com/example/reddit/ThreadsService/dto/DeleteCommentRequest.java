package com.example.reddit.ThreadsService.dto;

import java.util.UUID;

public class DeleteCommentRequest {
    private UUID commentId;

    public DeleteCommentRequest() {}

    public DeleteCommentRequest(UUID commentId) {
        this.commentId = commentId;
    }

    public UUID getCommentId() {
        return commentId;
    }

    public void setCommentId(UUID commentId) {
        this.commentId = commentId;
    }
}
