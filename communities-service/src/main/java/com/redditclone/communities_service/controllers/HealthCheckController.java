package com.redditclone.communities_service.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public/healthcheck")
public class HealthCheckController {

    @GetMapping
    public Map<String, Object> healthCheck() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "community-service");
        status.put("status", "UP");

        ZonedDateTime now = Instant.now().atZone(ZoneId.of("Africa/Cairo")); // Change zone as needed
        String formattedTime = now.format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a z"));
        status.put("timestamp", formattedTime);
        return status;
    }
}