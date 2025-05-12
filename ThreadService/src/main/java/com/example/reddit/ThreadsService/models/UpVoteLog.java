package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class UpVoteLog extends Log
{
    public UpVoteLog(UUID userId, UUID threadId) {
        super(userId, ActionType.UPVOTE, threadId);
    }

    @Override
    public String getLogType() {
        return "UpVoteLog";
    }
}
