package com.redditclone.notification_service.repositories;

import com.redditclone.notification_service.models.entity.UserNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserNotifyRepository extends MongoRepository<UserNotification, String> {
    List<UserNotification> findByUserId(UUID userId);

    List<UserNotification> findByUserIdAndStatus(UUID userId, String status);

    /**
     * Find the user-notification document for this user
     * whose Notification reference has the given id.
     */
    Optional<UserNotification> findByUserIdAndNotification_Id(
            UUID userId,
            String notificationId
    );

}
