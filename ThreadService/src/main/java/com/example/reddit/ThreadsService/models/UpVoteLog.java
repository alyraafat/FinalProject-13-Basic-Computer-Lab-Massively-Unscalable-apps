package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class UpVoteLog extends Log implements LogInterface
{
    //concrete product
    int upVoteCount;
    public UpVoteLog(UUID userId, UUID threadId, int upVoteCount) {
        super(userId, ActionType.UPVOTE, threadId);
        this.upVoteCount = upVoteCount;
    }

    @Override
    public String getLogType() {
        return "UpVoteLog";
    }

    @Override
    public Log createLog(UUID userId, UUID threadId) {
        return new UpVoteLog(userId, threadId, upVoteCount);
    }
}
