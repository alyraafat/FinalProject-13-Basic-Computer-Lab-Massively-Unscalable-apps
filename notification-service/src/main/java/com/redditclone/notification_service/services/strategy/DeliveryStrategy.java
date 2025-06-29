package com.redditclone.notification_service.services.strategy;

import com.redditclone.notification_service.models.entity.UserNotification;

public interface DeliveryStrategy {
    void deliver(UserNotification userNotification);
}