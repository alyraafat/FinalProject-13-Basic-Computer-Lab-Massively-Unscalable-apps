package com.example.reddit.ThreadsService.models;

import com.example.reddit.ThreadsService.repositories.LogRepository;
import com.example.reddit.ThreadsService.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;
@Component
public class LogReflectionFactory{

    @Autowired
    private LogService logService;
    public Log createLog(ActionType actionType, UUID userId, UUID threadId) {
        try {
            Class<?> clazz = Class.forName(actionType.getClassName());
            Log createdLog = (Log) clazz.getDeclaredConstructor(UUID.class, UUID.class).newInstance(userId, threadId);
            Method[] methods = clazz.getDeclaredMethods();
            System.out.println("Methods in " + clazz.getName() + ":");
            for (Method method : methods) {
                System.out.println(" - " + method.getName());
            }
            Method ManufactureLogMethod = clazz.getMethod("manufactureLog", UUID.class, UUID.class);
            Log log = (Log) ManufactureLogMethod.invoke(createdLog, userId, threadId);
            logService.createLog(createdLog);
            return log ;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create log for " + actionType, e);
        }
    }

   /* public static void main(String[] args) {
        // Example usage
        UUID userId = UUID.randomUUID();
        UUID threadId = UUID.randomUUID();

        // Using reflection-based factory method
        Log commentLog =
        System.out.println("Created log of type: " + commentLog.getLogType());
    }*/
}
