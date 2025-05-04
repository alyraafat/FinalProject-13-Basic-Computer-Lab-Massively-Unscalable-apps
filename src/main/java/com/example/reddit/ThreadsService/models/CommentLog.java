package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class CommentLog extends Log{
    public CommentLog(UUID userId, ActionType actionType, UUID threadId) {
        super(userId, actionType, threadId);
    }

    @Override
    public String getLogType() {
        return "CommentLog";
    }
}
