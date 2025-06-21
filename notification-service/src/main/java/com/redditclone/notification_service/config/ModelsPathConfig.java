package com.redditclone.notification_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "modelspath")
public class ModelsPathConfig {
    private String deliveryChannel;
    private String notification;
    private String notificationRequest;
    private String notificationResponse;
    private String notificationType;
    private String preferenceUpdate;
}
