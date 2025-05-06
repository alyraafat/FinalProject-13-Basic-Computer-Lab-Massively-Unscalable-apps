package com.example.miniapp.services.Factory;


import com.example.miniapp.services.Factory.impl.CommunityNotifier;
import com.example.miniapp.services.Factory.impl.ThreadNotifier;
import com.example.miniapp.services.Factory.impl.UserNotifier;
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

//    public Notifier create(NotificationType type, DeliveryStrategy strategy) {
//        return switch (type) {
//            case USER_SPECIFIC -> new UserNotifier(strategy);
//            case COMMUNITY -> new CommunityNotifier(strategy);
//            case THREAD -> new ThreadNotifier(strategy);
//        };
//    }
}