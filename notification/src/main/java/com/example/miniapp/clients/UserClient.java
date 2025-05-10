package com.example.miniapp.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "user-service", url  = "${user.service.url}")
public interface UserClient {
    @GetMapping("/api/users/get_emails")
    List<String> getEmailsByIds(@RequestParam("ids") List<UUID> ids);
}
