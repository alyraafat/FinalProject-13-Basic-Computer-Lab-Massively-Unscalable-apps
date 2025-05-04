package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class PostLog extends Log{
    public PostLog(UUID userId, ActionType actionType, UUID threadId) {
        super(userId, actionType, threadId);
    }

    @Override
    public String getLogType() {
        return "PostLog";
    }
}
