package com.example.reddit.ThreadsService.models;

import com.example.reddit.ThreadsService.repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

public class DownVoteLog extends Log
{
    //concrete product
    Integer downVoteCount;

    public DownVoteLog(UUID userId, UUID threadId, Integer downVoteCount) {
        super(userId, ActionType.DOWNVOTE, threadId,downVoteCount);
        this.downVoteCount=downVoteCount;

    }

    @Override
    public String getLogType() {
        return "DownVoteLog";
    }

    @Override
    public Log createLog(UUID userId, UUID threadId) {
//

        return new DownVoteLog(userId, threadId,this.downVoteCount );
    }


}