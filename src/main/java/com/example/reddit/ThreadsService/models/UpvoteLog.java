package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class UpvoteLog extends Log
{
    public UpvoteLog(UUID userId, ActionType actionType, UUID threadId) {
        super(userId, actionType, threadId);
    }

    @Override
    public String getLogType() {
        return "UpvoteLog";
    }
}
