package com.example.reddit.ThreadsService.models;

import com.example.reddit.ThreadsService.repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class UpVoteManufacturer extends Log{
    //concrete factories
    @Autowired
private ThreadRepository threadRepository;
    public UpVoteManufacturer(UUID userId, ActionType actionType, UUID threadId,
                              ThreadRepository threadRepository) {
        super(userId, actionType, threadId);
        this.threadRepository = threadRepository;
    }

    public Log UpVoteManufacturer(UUID userId, UUID threadId, int upVoteCount) {
        return new UpVoteLog(userId, threadId, upVoteCount);
    }

    @Override
    public String getLogType() {
        return "UpVoteLog";
    }

    @Override
    public Log manufactureLog(UUID userId, UUID threadId) {
        return new UpVoteLog(userId, threadId, threadRepository.findById(threadId).get().getUpVotes());
    }
}
