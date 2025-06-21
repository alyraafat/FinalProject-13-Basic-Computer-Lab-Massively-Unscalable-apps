package com.redditclone.thread_service.models;

import java.util.UUID;

public class UpVoteManufacturer extends LogManufacturer{
    //concrete factories
    UpVoteLog upVoteLog;



    public UpVoteManufacturer(UUID userId, ActionType actionType, UUID threadId, Integer upVoteCount) {

        this.upVoteLog = new UpVoteLog(userId, threadId,upVoteCount);

    }



    @Override
    public Log manufactureLog(UUID userId, UUID threadId) {
        return upVoteLog.createLog(userId, threadId );
    }
}