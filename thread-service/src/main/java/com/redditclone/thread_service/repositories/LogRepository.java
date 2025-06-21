package com.redditclone.thread_service.repositories;

import com.redditclone.thread_service.models.Log;
import com.redditclone.thread_service.models.ActionType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LogRepository extends MongoRepository<Log, UUID> {
    List<Log> findByUserId(UUID userId);
    List<Log> findByThreadId(UUID threadId);
    List<Log> findByActionType(ActionType actionType);
    List<Log> findByUserIdAndActionType(UUID userId, ActionType actionType);
}