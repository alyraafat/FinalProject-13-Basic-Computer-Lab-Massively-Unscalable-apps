package com.example.reddit.ThreadsService.models;

import com.example.reddit.ThreadsService.repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class UpVoteManufacturer extends LogManufacturer{
    //concrete factories
    UpVoteLog upVoteLog;
    @Autowired
    private ThreadRepository threadRepository;
    public UpVoteManufacturer(UUID userId, ActionType actionType, UUID threadId,
                              ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
        upVoteLog = new UpVoteLog(userId, threadId, 0, threadRepository);
    }

    public Log UpVoteManufacturer(UUID userId, UUID threadId, int upVoteCount) {
        return upVoteLog.createLog(userId, threadId);
    }


    @Override
    public Log manufactureLog(UUID userId, UUID threadId) {
        return upVoteLog.createLog(userId, threadId);
    }
}
