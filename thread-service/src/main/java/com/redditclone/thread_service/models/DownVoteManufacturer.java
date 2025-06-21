package com.redditclone.thread_service.models;

import java.util.UUID;

public class DownVoteManufacturer extends LogManufacturer {
    //concrete factories
    DownVoteLog downVoteLog;



    public DownVoteManufacturer(UUID userId, ActionType actionType, UUID threadId , Integer downVoteCount) {

        downVoteLog= new DownVoteLog(userId,threadId,downVoteCount);
    }



    @Override
    public Log manufactureLog(UUID userId, UUID threadId) {
        return downVoteLog.createLog(userId, threadId);
    }
}