package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class PostLog extends Log implements LogInterface{
    //concrete product
    public PostLog(UUID userId,UUID threadId) {
        super(userId, ActionType.POST, threadId);
    }


    @Override
    public Log createLog(UUID userId, UUID threadId) {
        return new PostLog(userId, threadId);
    }

    @Override
    public String getLogType() {
        return "PostLog";
    }

    @Override
    public Log manufactureLog(UUID userId, UUID threadId) {
        return null;
    }
}
