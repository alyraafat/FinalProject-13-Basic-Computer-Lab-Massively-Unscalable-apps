package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class PostManufacturer extends LogManufacturer{
    //concrete
    PostLog postLog;
    public PostManufacturer(UUID userId, ActionType actionType, UUID threadId) {
        postLog = new PostLog(userId, threadId);
    }


    @Override
    public Log manufactureLog(UUID userId, UUID threadId) {
        return postLog.createLog(userId, threadId);
    }
}