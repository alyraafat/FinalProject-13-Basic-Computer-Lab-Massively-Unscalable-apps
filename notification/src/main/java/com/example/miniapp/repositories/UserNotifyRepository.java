package com.example.miniapp.repositories;

import com.example.miniapp.models.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotifyRepository  extends JpaRepository<UserNotification, String> {
}
