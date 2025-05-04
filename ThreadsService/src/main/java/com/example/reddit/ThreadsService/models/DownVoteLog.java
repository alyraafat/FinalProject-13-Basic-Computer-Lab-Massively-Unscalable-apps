package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class DownVoteLog extends Log
{
    public DownVoteLog(UUID userId, ActionType actionType, UUID threadId) {
        super(userId, actionType, threadId);
    }

    @Override
    public String getLogType() {
        return "DownVoteLog";
    }
}
