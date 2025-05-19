package com.example.reddit.ThreadsService.models;

import com.example.reddit.ThreadsService.repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class UpVoteLog extends Log
{
    //concrete product
    int upVoteCount;
    @Autowired
    private ThreadRepository threadRepository;
    public UpVoteLog(UUID userId, UUID threadId, int upVoteCount,
                     ThreadRepository threadRepository) {
        super(userId, ActionType.UPVOTE, threadId);
        this.upVoteCount = upVoteCount;
        this.threadRepository = threadRepository;
    }

    @Override
    public String getLogType() {
        return "UpVoteLog";
    }


    @Override
    public Log createLog(UUID userId, UUID threadId) {
        return new UpVoteLog(userId, threadId, threadRepository.findById(threadId).get().getUpVotes(), threadRepository);
    }
}
