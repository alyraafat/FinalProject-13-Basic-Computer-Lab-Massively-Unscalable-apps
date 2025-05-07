package com.example.moderator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCommentRequest {
    private UUID commentId;
    private UUID threadId;
    private UUID moderatorId;
}
