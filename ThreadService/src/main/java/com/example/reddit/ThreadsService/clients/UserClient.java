package com.example.reddit.ThreadsService.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {
    @GetMapping("/users/{userId}/block/allblocks")
    List<String> getBlockList(@PathVariable("userId") String userId);
}
