package com.example.reddit.ThreadsService.models;

import com.example.reddit.ThreadsService.repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class DownVoteManufacturer extends Log {
    //concrete factories
    @Autowired
    private ThreadRepository threadRepository;
    public DownVoteManufacturer(UUID userId, ActionType actionType, UUID threadId,
                                ThreadRepository threadRepository) {
        super(userId, actionType, threadId);
        this.threadRepository = threadRepository;
    }

    public Log manufactureLog(UUID userId, UUID threadId,int downVoteCount) {
        return new DownVoteLog(userId, threadId, downVoteCount);
    }

    @Override
    public String getLogType() {
        return null;
    }

    @Override
    public Log manufactureLog(UUID userId, UUID threadId) {
        return new DownVoteLog(userId, threadId, threadRepository.findById(threadId).get().getDownVotes());
    }
}
