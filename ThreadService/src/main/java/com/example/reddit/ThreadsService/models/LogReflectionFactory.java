package com.example.reddit.ThreadsService.models;

public class LogReflectionFactory {
    public static Log createLog(ActionType actionType) {
        try {
            Class<?> clazz = Class.forName(actionType.getClassName());
            return (Log) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create log for " + actionType, e);
        }
    }
}
