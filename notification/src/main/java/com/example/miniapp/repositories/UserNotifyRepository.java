package com.example.miniapp.repositories;

import com.example.miniapp.models.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserNotifyRepository  extends JpaRepository<UserNotification, String> {
    List<UserNotification> findByUserId(UUID userId);
    List<UserNotification> findByUserIdAndStatus(UUID userId, String status);
}
