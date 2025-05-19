package com.example.reddit.ThreadsService.models;

import com.example.reddit.ThreadsService.repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class DownVoteManufacturer extends LogManufacturer {
    //concrete factories
    DownVoteLog DownVoteLog;
    @Autowired
    private ThreadRepository threadRepository;
    public DownVoteManufacturer(UUID userId, ActionType actionType, UUID threadId,
                                ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;

    }

    public Log manufactureLog(UUID userId, UUID threadId,int downVoteCount) {
        return new DownVoteLog(userId, threadId, downVoteCount, threadRepository);
    }

    @Override
    public Log manufactureLog(UUID userId, UUID threadId) {
        return DownVoteLog.createLog(userId, threadId);
    }
}
