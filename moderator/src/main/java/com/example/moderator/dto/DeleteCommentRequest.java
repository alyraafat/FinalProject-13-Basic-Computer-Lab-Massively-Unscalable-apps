package com.example.moderator.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeleteCommentRequest {
    private UUID commentId;

    public DeleteCommentRequest() {}

    public DeleteCommentRequest(UUID commentId) {
        this.commentId = commentId;
    }
}
