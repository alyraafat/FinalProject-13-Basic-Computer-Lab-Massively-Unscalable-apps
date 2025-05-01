package com.example.miniapp.services;

import com.example.miniapp.models.NotificationRequest;
import com.example.miniapp.services.Factory.Notifier;
import com.example.miniapp.services.Factory.NotifierFactory;
import com.example.miniapp.services.strategy.DeliveryStrategy;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotifierFactory factory;
    private final PreferenceService preferenceService;

    public NotificationService(NotifierFactory factory, PreferenceService preferenceService) {
        this.factory = factory;
        this.preferenceService = preferenceService;
    }

    public void process(NotificationRequest request) {
        DeliveryStrategy strategy = preferenceService.getStrategy(request.getUserId());
        Notifier notifier = factory.create(request.getType(), strategy);
        notifier.notify(request);
    }
}