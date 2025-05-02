package com.example.reddit.CommunitiesService.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class NotificationClient {
    private final RestTemplate rest;
    private final String base = "http://notification-service/api/notifications";

    @Autowired
    public NotificationClient(RestTemplate rest) {
        this.rest = rest;
    }

    public void notifyMemberAdded(UUID communityId, UUID userId) {
        // You can send a full payload object if your Notification API expects JSON
        String url = base + "/community-member-added";
        rest.postForEntity(
                url,
                new NotificationRequest(communityId, userId),
                Void.class
        );
    }

    private static class NotificationRequest {
        public UUID communityId;
        public UUID userId;
        public NotificationRequest(UUID communityId, UUID userId) {
            this.communityId = communityId;
            this.userId = userId;
        }
    }
}
