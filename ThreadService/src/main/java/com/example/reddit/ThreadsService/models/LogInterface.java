package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public interface LogInterface {
    //prodcut interface

    public Log createLog(UUID userId, UUID threadId);
}
