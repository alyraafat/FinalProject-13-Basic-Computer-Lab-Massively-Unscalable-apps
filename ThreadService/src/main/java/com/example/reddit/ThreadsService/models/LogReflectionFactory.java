package com.example.reddit.ThreadsService.models;

import java.util.UUID;

public class LogReflectionFactory {
    public static Log createLog(ActionType actionType, UUID userId, UUID threadId) {
        try {
            Class<?> clazz = Class.forName(actionType.getClassName());
            return (Log) clazz.getDeclaredConstructor(UUID.class, UUID.class).newInstance(userId, threadId);
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
