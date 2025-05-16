package com.example.reddit.ThreadsService.models;

import com.example.reddit.ThreadsService.repositories.LogRepository;
import com.example.reddit.ThreadsService.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class LogReflectionFactory {

    @Autowired
    private LogService logService;
    public Log createLog(ActionType actionType, UUID userId, UUID threadId) {
        try {
            Class<?> clazz = Class.forName(actionType.getClassName());
            Log createdLog = (Log) clazz.getDeclaredConstructor(UUID.class, UUID.class).newInstance(userId, threadId);
            logService.createLog(createdLog);
            return createdLog ;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create log for " + actionType, e);
        }
    }

   /* public static void main(String[] args) {
        // Example usage
        UUID userId = UUID.randomUUID();
        UUID threadId = UUID.randomUUID();

        // Using reflection-based factory method
        Log commentLog = createLog(ActionType.COMMENT, userId, threadId);
        System.out.println("Created log of type: " + commentLog.getLogType());
    }*/
}
