package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class UpvoteLog extends Log
{
    public UpvoteLog(UUID userId, UUID threadId) {
        super(userId, ActionType.UPVOTE, threadId);
    }

    @Override
    public String getLogType() {
        return "UpvoteLog";
    }
}
