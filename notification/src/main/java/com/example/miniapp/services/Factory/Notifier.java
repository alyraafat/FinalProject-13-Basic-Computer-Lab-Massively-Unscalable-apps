package com.example.miniapp.services.Factory;

import com.example.miniapp.models.dto.NotificationRequest;

public interface Notifier {
    void notify(NotificationRequest request);
}
