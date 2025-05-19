package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class DownVoteLog extends Log implements LogInterface
{
    //concrete product
    int downVoteCount;
    public DownVoteLog(UUID userId, UUID threadId, int downVoteCount) {
        super(userId, ActionType.DOWNVOTE, threadId);
        this.downVoteCount = downVoteCount;
    }

    @Override
    public String getLogType() {
        return "DownVoteLog";
    }

    @Override
    public Log manufactureLog(UUID userId, UUID threadId) {
        return null;
    }

    @Override
    public Log createLog(UUID userId, UUID threadId) {
        return new DownVoteLog(userId, threadId, downVoteCount);
    }
}
