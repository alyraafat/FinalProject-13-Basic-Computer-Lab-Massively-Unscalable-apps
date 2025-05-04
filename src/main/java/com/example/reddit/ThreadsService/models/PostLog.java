package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class PostLog extends Log{
    public PostLog(UUID userId,UUID threadId) {
        super(userId, ActionType.POST, threadId);
    }

    @Override
    public String getLogType() {
        return "PostLog";
    }
}
