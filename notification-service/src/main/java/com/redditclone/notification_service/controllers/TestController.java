package com.redditclone.notification_service.controllers;

import com.redditclone.notification_service.clients.UserClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private UserClient userClient;

    public TestController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/test")
    public List<String> test() {
        UUID uuid = UUID.randomUUID();
        List<UUID> uuids = new ArrayList<>();
        uuids.add(uuid);
        List<String> emails = userClient.getEmailsByIds(uuids);
        System.out.println(emails);
        return emails;
    }
}
