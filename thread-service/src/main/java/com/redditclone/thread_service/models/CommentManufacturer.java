package com.redditclone.thread_service.models;

import java.util.UUID;

public class CommentManufacturer extends LogManufacturer{
//concrete factories

    CommentLog commentLog;
    public CommentManufacturer(UUID userId, ActionType actionType, UUID threadId) {
        this.commentLog = new CommentLog(userId, threadId);
    }
    @Override
    public Log manufactureLog(UUID userId, UUID threadId){
        return commentLog.createLog(userId, threadId);
    }


}