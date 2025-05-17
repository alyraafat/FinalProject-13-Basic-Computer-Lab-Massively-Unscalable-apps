package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class CommentLog extends Log implements LogInterface{
    //concrete product
    public CommentLog(UUID userId, UUID threadId) {
        super(userId, ActionType.COMMENT, threadId);
    }

    @Override
    public String getLogType() {
        return "CommentLog";
    }

    @Override
    public Log createLog(UUID userId, UUID threadId) {
        return new CommentLog(userId, threadId);
    }
}
