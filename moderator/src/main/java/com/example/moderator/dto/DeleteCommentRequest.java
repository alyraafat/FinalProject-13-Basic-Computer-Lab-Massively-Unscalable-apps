package com.example.moderator.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeleteCommentRequest {
    private UUID commentId;
    private UUID threadId;
    public DeleteCommentRequest() {}

    public DeleteCommentRequest(UUID commentId, UUID threadId) {
        this.commentId = commentId;
        this.threadId = threadId;
    }
}
