package com.redditclone.notification_service.services;


import com.redditclone.notification_service.models.entity.UserNotification;
import com.redditclone.notification_service.services.strategy.DeliveryStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SendNotificationStrategyService {
    private DeliveryStrategy deliveryStrategy;

    public SendNotificationStrategyService(@Qualifier("emailStrategy") DeliveryStrategy deliveryStrategy) {
        this.deliveryStrategy = deliveryStrategy;
    }

    public void setDeliveryStrategy(DeliveryStrategy deliveryStrategy) {
        this.deliveryStrategy = deliveryStrategy;
    }

    public void deliver(UserNotification userNotification) {
        deliveryStrategy.deliver(userNotification);
    }

}
