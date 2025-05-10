package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class CommentLog extends Log{
    public CommentLog(UUID userId, UUID threadId) {
        super(userId, ActionType.COMMENT, threadId);
    }

    @Override
    public String getLogType() {
        return "CommentLog";
    }
}
