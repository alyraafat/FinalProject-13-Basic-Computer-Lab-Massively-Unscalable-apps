package com.example.miniapp.services.Factory.impl;

import com.example.miniapp.models.NotificationRequest;
import com.example.miniapp.services.Factory.Notifier;
import com.example.miniapp.services.strategy.DeliveryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SecurityNotifier implements Notifier {
    private final DeliveryStrategy strategy;

    @Autowired
    public SecurityNotifier(@Qualifier("pushStrategy") DeliveryStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void notify(NotificationRequest request) {
//        String message = String.format("Security alert: %s", request.getDetails());
        String message = "Hello";
        strategy.deliver(request.getUserId(), message);
    }
}