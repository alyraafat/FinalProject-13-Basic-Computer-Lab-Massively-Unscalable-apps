package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class DownVoteLog extends Log
{
    public DownVoteLog(UUID userId, UUID threadId) {
        super(userId, ActionType.DOWNVOTE, threadId);
    }

    @Override
    public String getLogType() {
        return "DownVoteLog";
    }
}
