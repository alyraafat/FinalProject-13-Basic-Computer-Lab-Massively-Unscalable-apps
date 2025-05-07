package com.example.miniapp.services.Factory;

import com.example.miniapp.services.Factory.impl.CommunityNotifier;

import com.example.miniapp.models.enums.NotificationType;
import com.example.miniapp.models.enums.DeliveryChannel;
import com.example.miniapp.services.Factory.impl.UserNotifier;
import com.example.miniapp.services.Factory.impl.ThreadNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotifierFactory {

    private final UserNotifier userNotifier;
    private final CommunityNotifier communityNotifier;
    private final ThreadNotifier threadNotifier;

    @Autowired
    public NotifierFactory(
            UserNotifier userNotifier,
            CommunityNotifier communityNotifier,
            ThreadNotifier threadNotifier) {
        this.userNotifier = userNotifier;
        this.communityNotifier = communityNotifier;
        this.threadNotifier = threadNotifier;
    }

    public Notifier create(NotificationType type, DeliveryChannel strategy) {
        return switch (type) {
            case USER_SPECIFIC -> {
                userNotifier.setDeliveryStrategy(strategy);
                yield userNotifier;
            }
            case COMMUNITY -> {
                communityNotifier.setDeliveryStrategy(strategy);
                yield communityNotifier;
            }
            case THREAD -> {
                threadNotifier.setDeliveryStrategy(strategy);
                yield threadNotifier;
            }
            default -> throw new IllegalArgumentException("Unknown notification type: " + type);
        };
    }
}