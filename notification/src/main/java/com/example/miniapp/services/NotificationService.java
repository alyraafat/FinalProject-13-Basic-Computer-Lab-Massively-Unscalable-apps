package com.example.miniapp.services;

import com.example.miniapp.models.dto.NotificationRequest;
import com.example.miniapp.models.enums.DeliveryChannel;
import com.example.miniapp.repositories.NotificationRepository;
import com.example.miniapp.services.Factory.Notifier;
import com.example.miniapp.services.Factory.NotifierFactory;
import com.example.miniapp.services.strategy.DeliveryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.miniapp.models.enums.NotificationType;
import com.example.miniapp.models.entity.Notification;

import java.time.Instant;


@Service
public class NotificationService {
    private final NotifierFactory notifierFactory;
    private final PreferenceService preferenceService;
    private final NotificationRepository notificationRepository;

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    public NotificationService(NotifierFactory factory, PreferenceService preferenceService, NotificationRepository notificationRepository) {
        this.notifierFactory = factory;
        this.preferenceService = preferenceService;
        this.notificationRepository = notificationRepository;
    }


    public void process(NotificationRequest request) {

        NotificationType type = request.getType();
        Notification notification = mapRequestToEntity(request);
        notificationRepository.save(notification);

        Notifier notifier = notifierFactory.create(type);
        notifier.notify(notification);
    }

    private Notification mapRequestToEntity(NotificationRequest request) {
        Notification notification = new Notification();

        // Core fields
        notification.setType(request.getType().toString());
        notification.setMessage(request.getRawMessage());
        notification.setCreatedAt(Instant.now());
        notification.setReceiverId(request.getReceiverId());
        // Additional fields you might want to set
        notification.setTitle(generateTitleFromType(request.getType()));
        notification.setSenderId(request.getActorUserId()); // Or another sender identifier

        return notification;
    }

    private String generateTitleFromType(NotificationType type) {
        switch(type) {
            case COMMUNITY: return "Community Update";
            case THREAD: return "New Thread Activity";
            case USER_SPECIFIC: return "User Notification";
            default: return "Notification";
        }
    }

        // For push notifications
    public boolean sendPush(String formattedPayload) {
        try {
            logger.info("Sending push notification: {}", formattedPayload);
            System.out.println("[PUSH] " + formattedPayload);
            return true; // Assume success for demo
        } catch (Exception e) {
            logger.error("Failed to send push notification", e);
            return false;
        }
    }

    // For email notifications
    public boolean sendEmail(String emailContent) {
        try {
            logger.info("Sending email notification");
            System.out.println("[EMAIL]\n" + emailContent);
            return true; // Assume success for demo
        } catch (Exception e) {
            logger.error("Failed to send email notification", e);
            return false;
        }
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
