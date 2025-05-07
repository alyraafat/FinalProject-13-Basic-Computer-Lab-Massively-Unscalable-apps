package com.example.reddit.ThreadsService.dto;

import java.util.UUID;

public class ReportRequest {
    private UUID threadId;
    private String content;

    public ReportRequest() {}
    public ReportRequest(UUID threadId, String content) {
        this.threadId = threadId;
        this.content = content;
    }

    public UUID getThreadId() {
        return threadId;
    }

    public String getContent() {
        return content;
    }

    public void setThreadId(UUID threadId) {
        this.threadId = threadId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
