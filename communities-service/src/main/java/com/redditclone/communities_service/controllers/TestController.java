package com.redditclone.communities_service.controllers;

import com.redditclone.communities_service.models.Community;
import com.redditclone.communities_service.rabbitmq.CommunityProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/tests")
public class TestController {
    private CommunityProducer communityProducer;

    public TestController(CommunityProducer communityProducer) {
        this.communityProducer = communityProducer;
    }

    @GetMapping
    public void test() {
        UUID id = UUID.randomUUID();
        String name = "kimo";
        UUID topicId = UUID.randomUUID();
        String description = "This is a test";
        Community community = Community.builder()
                .name(name)
                .description(description)
                .topicId(topicId)
                .id(id)
                .build();
        communityProducer.notifyNewThread(community);
    }

    @GetMapping("/test2")
    public void test2() {
        UUID id = UUID.randomUUID();
        String name = "kimo";
        UUID topicId = UUID.randomUUID();
        String description = "This is a test";
        Community community = Community.builder()
                .name(name)
                .description(description)
                .topicId(topicId)
                .id(id)
                .build();
        communityProducer.notifyNewMember(topicId, community );
    }
}
