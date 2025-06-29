// This file is for testing leave until we finish all the communication.


//package com.example.reddit.CommunitiesService.controllers;
//
//import com.example.reddit.CommunitiesService.models.Community;
//import com.example.reddit.CommunitiesService.rabbitmq.CommunityProducer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/test/communities")
//public class CommunicationTestController {
//    private final CommunityProducer communityProducer;
//
//    @Autowired
//    public CommunicationTestController(CommunityProducer communityProducer) {
//        this.communityProducer = communityProducer;
//    }
//
//    @GetMapping
//    public void test() {
//        UUID communityId = UUID.randomUUID();
//        communityProducer.notifyMemberAdded(communityId);
//    }
//
//}