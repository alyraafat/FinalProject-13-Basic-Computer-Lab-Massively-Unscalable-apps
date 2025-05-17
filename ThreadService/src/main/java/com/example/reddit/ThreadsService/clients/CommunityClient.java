package com.example.reddit.ThreadsService.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "community-service", url = "http://community-service:8091/communities")
public interface CommunityClient {

    @GetMapping("/{id}/is-banned/{userId}")
    public boolean isUserBanned(@PathVariable UUID id, @PathVariable UUID userId);
}
