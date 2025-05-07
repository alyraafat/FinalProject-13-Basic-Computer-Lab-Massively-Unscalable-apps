package com.example.miniapp.services;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.enums.DeliveryChannel;
import com.example.miniapp.services.Factory.Notifier;
import com.example.miniapp.services.Factory.NotifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.miniapp.models.enums.NotificationType;


@Service
public class NotificationService {
    private final NotifierFactory notifierFactory;
    private final PreferenceService preferenceService;

    @Autowired
    public NotificationService(NotifierFactory factory, PreferenceService preferenceService) {
        this.notifierFactory = factory;
        this.preferenceService = preferenceService;
    }


    public void process(NotificationRequest request) {
        //TODO: use it to get the way of sending and return a type enum... Temp: strategy var below
//        DeliveryStrategy strategy = preferenceService
//                .getPreferredStrategy(request.getUserId());



        NotificationType type = request.getType();
//        to be removed
        DeliveryChannel strategy = DeliveryChannel.PUSH;

//        Notifier notifier = notifierFactory.create(request.getType(), strategy);
        Notifier notifier = notifierFactory.create(type, strategy);
        notifier.notify(request);
    }
}


//import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//
//@Service
//public class NotificationService {
//    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
//
//    // For push notifications
//    public boolean sendPush(String formattedPayload) {
//        try {
//            logger.info("Sending push notification: {}", formattedPayload);
//            System.out.println("[PUSH] " + formattedPayload);
//            return true; // Assume success for demo
//        } catch (Exception e) {
//            logger.error("Failed to send push notification", e);
//            return false;
//        }
//    }
//
//    // For email notifications
//    public boolean sendEmail(String emailContent) {
//        try {
//            logger.info("Sending email notification");
//            System.out.println("[EMAIL]\n" + emailContent);
//            return true; // Assume success for demo
//        } catch (Exception e) {
//            logger.error("Failed to send email notification", e);
//            return false;
//        }
//    }
//
//}
