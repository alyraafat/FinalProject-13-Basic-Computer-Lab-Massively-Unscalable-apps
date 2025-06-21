package com.redditclone.communities_service.clients;

import com.redditclone.communities_service.models.CommunityThread;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "thread-service", url = "http://thread-service:8090/threads")
public interface ThreadClient {

    @GetMapping("/{id}")
    public CommunityThread getThreadById(@PathVariable UUID id);
}

