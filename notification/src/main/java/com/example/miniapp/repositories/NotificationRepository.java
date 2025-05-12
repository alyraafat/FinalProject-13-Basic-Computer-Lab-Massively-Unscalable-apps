package com.example.miniapp.repositories;

import com.example.miniapp.models.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository  extends MongoRepository<Notification, UUID> {


}