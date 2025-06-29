package com.redditclone.thread_service.services;

import com.redditclone.thread_service.models.Log;
import com.redditclone.thread_service.models.ActionType;
import com.redditclone.thread_service.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LogService {
    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

    public Optional<Log> getLogById(UUID id) {
        return logRepository.findById(id);
    }

    public Log createLog(Log log) {
        return logRepository.save(log);
    }

    public void deleteLog(UUID id) {
        logRepository.deleteById(id);
    }

    public List<Log> getLogsByUser(UUID userId) {
        return logRepository.findByUserId(userId);
    }

    public List<Log> getLogsByThread(UUID threadId) {
        return logRepository.findByThreadId(threadId);
    }



    public List<Log> getLogsByActionType(ActionType actionType) {
        return logRepository.findByActionType(actionType);
    }

    public List<Log> getLogsByUserAndActionType(UUID userId, ActionType actionType) {
        return logRepository.findByUserIdAndActionType(userId, actionType);
    }
}