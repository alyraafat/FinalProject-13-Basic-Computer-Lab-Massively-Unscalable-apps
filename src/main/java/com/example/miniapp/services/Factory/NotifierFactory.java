package com.example.miniapp.services.Factory;


import com.example.miniapp.models.NotificationType;
import com.example.miniapp.services.Factory.impl.CommunityNotifier;
import com.example.miniapp.services.Factory.impl.SecurityNotifier;
import com.example.miniapp.services.Factory.impl.ThreadNotifier;
import com.example.miniapp.services.strategy.DeliveryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotifierFactory {

    private final SecurityNotifier securityNotifier;
    private final CommunityNotifier communityNotifier;
    private final ThreadNotifier threadNotifier;

    @Autowired
    public NotifierFactory(
            SecurityNotifier securityNotifier,
            CommunityNotifier communityNotifier,
            ThreadNotifier threadNotifier) {
        this.securityNotifier = securityNotifier;
        this.communityNotifier = communityNotifier;
        this.threadNotifier = threadNotifier;
    }

    public Notifier create(NotificationType type, DeliveryStrategy strategy) {
        return switch (type) {
            case SECURITY -> {
                securityNotifier.setDeliveryStrategy(strategy);
                yield securityNotifier;
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