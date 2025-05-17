package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class CommentManufacturer extends Log{
//concrete factories
    public CommentManufacturer(UUID userId, ActionType actionType, UUID threadId) {
        super(userId, actionType, threadId);
    }
    public Log manufactureLog(UUID userId, UUID threadId){
        return new CommentLog(userId, threadId);
    }

    @Override
    public String getLogType() {
        return null;
    }
}
