package com.example.moderator.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class ReportRequest {
    private UUID threadId;
    private String content;

    public ReportRequest() {}
    public ReportRequest(UUID threadId, String content) {
        this.threadId = threadId;
        this.content = content;
    }
}
