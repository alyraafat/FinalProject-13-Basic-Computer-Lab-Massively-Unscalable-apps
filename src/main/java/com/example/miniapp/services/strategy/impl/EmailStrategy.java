package com.example.miniapp.services.strategy.impl;

import com.example.miniapp.services.strategy.DeliveryStrategy;
import org.springframework.stereotype.Service;

@Service("emailStrategy")
public class EmailStrategy implements DeliveryStrategy {
    @Override
    public void deliver(String userId, String message) {
        // Email sending logic
    }
}