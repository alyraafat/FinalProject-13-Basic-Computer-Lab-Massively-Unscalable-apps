package com.redditclone.notification_service.repositories;

import com.redditclone.notification_service.models.entity.PushReceived;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PushReceivedRepository extends MongoRepository<PushReceived, String> {
}
