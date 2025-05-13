package com.example.reddit.CommunitiesService.clients;

import com.example.reddit.CommunitiesService.models.CommunityThread;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "moderator-service", url = "http://moderator-service:8086/moderator")
public interface ModeratorClient {

    @PostMapping
    public ResponseEntity<Object> addModerator(
            @RequestParam("userId") UUID userId,
            @RequestParam("communityId") UUID communityId,
            @RequestHeader(value = "X-User-Id", required = false) String userHeaderId
    );
}

