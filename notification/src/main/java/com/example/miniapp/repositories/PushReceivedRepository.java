package com.example.miniapp.repositories;

import com.example.miniapp.models.entity.PushReceived;
import com.example.miniapp.models.entity.UserNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface PushReceivedRepository  extends MongoRepository<PushReceived, UUID> {
}
