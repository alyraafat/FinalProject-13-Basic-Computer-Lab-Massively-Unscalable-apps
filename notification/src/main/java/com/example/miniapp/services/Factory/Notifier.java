package com.example.miniapp.services.Factory;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.enums.DeliveryChannel;

public interface Notifier {

    void notify(NotificationRequest request);
    void setDeliveryStrategy(DeliveryChannel strategy);
}
