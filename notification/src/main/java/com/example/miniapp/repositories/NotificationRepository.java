package com.example.miniapp.repositories;

import com.example.miniapp.models.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    // Find notifications by receiver ID (through user_notifications join)
    @Query("SELECT n FROM Notification n JOIN UserNotification un ON n.id = un.notification.id WHERE un.userId = :userId")
    Page<Notification> findByUserId(String userId, Pageable pageable);

    // Find unread notifications for a user
    @Query("SELECT n FROM Notification n JOIN UserNotification un ON n.id = un.notification.id " +
            "WHERE un.userId = :userId AND un.status = 'UNREAD'")
    List<Notification> findUnreadByUserId(String userId);

    // Find notifications created after a certain date
    List<Notification> findByCreatedAtAfter(Instant date);

    // Find notifications by type
    Page<Notification> findByType(String type, Pageable pageable);

    // Count unread notifications for a user
    @Query("SELECT COUNT(un) FROM UserNotification un WHERE un.userId = :userId AND un.status = 'UNREAD'")
    long countUnreadByUserId(String userId);
}