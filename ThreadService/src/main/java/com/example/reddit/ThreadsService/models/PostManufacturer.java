package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class PostManufacturer extends Log{
    //concrete factories
    public PostManufacturer(UUID userId, ActionType actionType, UUID threadId) {
        super(userId, actionType, threadId);
    }

    @Override
    public String getLogType() {
        return null;
    }

    @Override
    public Log manufactureLog(UUID userId, UUID threadId) {
        return new PostLog(userId, threadId);
    }
}
