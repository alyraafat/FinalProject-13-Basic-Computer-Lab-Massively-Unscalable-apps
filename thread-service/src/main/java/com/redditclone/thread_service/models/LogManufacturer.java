package com.redditclone.thread_service.models;

import java.util.UUID;

public abstract class LogManufacturer {


    public abstract Log manufactureLog(UUID userId, UUID threadId);

}