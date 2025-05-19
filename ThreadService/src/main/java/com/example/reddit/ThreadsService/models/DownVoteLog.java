package com.example.reddit.ThreadsService.models;

import com.example.reddit.ThreadsService.repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class DownVoteLog extends Log
{
    //concrete product
    int downVoteCount;
    @Autowired
    private ThreadRepository threadRepository;
    public DownVoteLog(UUID userId, UUID threadId, int downVoteCount,
                       ThreadRepository threadRepository) {
        super(userId, ActionType.DOWNVOTE, threadId);
        this.downVoteCount = downVoteCount;
        this.threadRepository = threadRepository;
    }

    @Override
    public String getLogType() {
        return "DownVoteLog";
    }

    @Override
    public Log createLog(UUID userId, UUID threadId) {
        return new DownVoteLog(userId, threadId, threadRepository.findById(threadId).get().getDownVotes(), threadRepository);
    }


}
