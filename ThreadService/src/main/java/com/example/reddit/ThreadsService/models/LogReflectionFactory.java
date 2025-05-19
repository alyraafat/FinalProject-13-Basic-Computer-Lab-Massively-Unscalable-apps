package com.example.reddit.ThreadsService.models;

import com.example.reddit.ThreadsService.repositories.LogRepository;
import com.example.reddit.ThreadsService.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.reddit.ThreadsService.repositories.ThreadRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.UUID;
@Component
public class LogReflectionFactory{

    @Autowired
    private LogService logService;
    @Autowired
    private ThreadRepository threadRepository;

    public Log createLog(ActionType actionType, UUID userId, UUID threadId) {
        try {
            Class<?> clazz = Class.forName(actionType.getClassName());
            LogManufacturer createdLog;
            System.out.println("class name " + clazz.getName());

            try {
                // Try to instantiate with three parameters first
                Constructor<?> constructor=  clazz.getDeclaredConstructor(UUID.class, ActionType.class, UUID.class);
                constructor.setAccessible(true);
                createdLog= (LogManufacturer) constructor.newInstance(userId, actionType, threadId);
            } catch (Exception e) {
                int count =0;
                if(clazz.getName().equals("com.example.reddit.ThreadsService.models.UpVoteManufacturer"))
                {
                     count=threadRepository.findById(threadId).get().getUpVotes();
                }
                else if (clazz.getName().equals("com.example.reddit.ThreadsService.models.DownVoteManufacturer"))
                {
                     count=threadRepository.findById(threadId).get().getDownVotes();
                }

                // If that fails, try with four parameters including ThreadRepository
                Constructor<?> constructor=  clazz.getDeclaredConstructor(UUID.class, ActionType.class, UUID.class,Integer.class);
                constructor.setAccessible(true);
                createdLog= (LogManufacturer) constructor.newInstance(userId, actionType, threadId ,count);
            }

            // Debug code to print available methods
            Method[] methods = clazz.getDeclaredMethods();
            System.out.println("Methods in " + clazz.getName() + ":");
            for (Method method : methods) {
                System.out.println(" - " + method.getName());
            }

            Method manufactureLogMethod = clazz.getDeclaredMethod("manufactureLog", UUID.class, UUID.class);
            manufactureLogMethod.setAccessible(true);
            Log log = (Log) manufactureLogMethod.invoke(createdLog, userId, threadId);

            logService.createLog(log);
            return log;
        } catch (Exception e) {
            e.printStackTrace(); // This will help you see the full error stack trace
            throw new RuntimeException("Failed to create log for " + actionType, e);
        }
    }

   public static void main(String[] args) {
        // Example usage
        UUID userId = UUID.randomUUID();
        UUID threadId = UUID.randomUUID();

        // Using reflection-based factory method
        Log commentLog =  new LogReflectionFactory().createLog(ActionType.COMMENT,userId,threadId);
        System.out.println("Created log of type: " + commentLog.getLogType());
    }
}

