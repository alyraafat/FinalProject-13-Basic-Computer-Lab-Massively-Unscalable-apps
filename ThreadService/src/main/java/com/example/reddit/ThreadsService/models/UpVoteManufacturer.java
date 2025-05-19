package com.example.reddit.ThreadsService.models;

import com.example.reddit.ThreadsService.repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

public class UpVoteManufacturer extends LogManufacturer{
    //concrete factories
    UpVoteLog upVoteLog;



    public UpVoteManufacturer(UUID userId, ActionType actionType, UUID threadId, Integer upVoteCount) {

        this.upVoteLog = new UpVoteLog(userId, threadId,upVoteCount);

    }



    @Override
    public Log manufactureLog(UUID userId, UUID threadId) {
        return upVoteLog.createLog(userId, threadId );
    }
}
