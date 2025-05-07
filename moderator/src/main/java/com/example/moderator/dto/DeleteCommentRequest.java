package com.example.moderator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
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
}
