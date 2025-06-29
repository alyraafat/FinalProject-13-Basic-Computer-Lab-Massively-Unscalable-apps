package com.redditclone.thread_service.models;

import java.util.UUID;

public class UpVoteLog extends Log
{
    //concrete product
    Integer upVoteCount;


    public UpVoteLog(UUID userId, UUID threadId, Integer upVoteCount) {

        super(userId, ActionType.UPVOTE, threadId,upVoteCount);
        this.upVoteCount=upVoteCount;


    }

    @Override
    public String getLogType() {
        return "UpVoteLog";
    }


    @Override
    public Log createLog(UUID userId, UUID threadId) {


        return new UpVoteLog(userId,threadId, this.upVoteCount);
    }
}