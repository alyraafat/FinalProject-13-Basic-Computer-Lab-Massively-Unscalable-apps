package com.example.reddit.CommunitiesService.config;


import com.example.reddit.CommunitiesService.clients.ModeratorClient;
import com.example.reddit.CommunitiesService.models.Community;
import com.example.reddit.CommunitiesService.models.SubTopic;
import com.example.reddit.CommunitiesService.models.Topic;
import com.example.reddit.CommunitiesService.repositories.CommunityRepository;
import com.example.reddit.CommunitiesService.repositories.SubTopicRepository;
import com.example.reddit.CommunitiesService.services.CommunityService;
import com.example.reddit.CommunitiesService.services.SubTopicService;
import com.example.reddit.CommunitiesService.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;


@Configuration
public class DataSeeder {

    @Autowired
    private final TopicService topicService;

    @Autowired
    private final SubTopicService subTopicService;

    @Autowired
    private final CommunityRepository communityRepo;

    @Autowired
    private final SubTopicRepository subTopicRepository;

    @Autowired
    private final CommunityService communityService;

    public DataSeeder(TopicService topicService,
                      SubTopicService subTopicService,
                      CommunityRepository communityRepo,
                      SubTopicRepository subTopicRepository,
                      CommunityService communityService) {
        this.topicService      = topicService;
        this.subTopicService   = subTopicService;
        this.communityRepo     = communityRepo;
        this.subTopicRepository = subTopicRepository;
        this.communityService  = communityService;
    }

    /**
     * Main runner that seeds data; run() is transactional to prevent stale-object errors.
     */
    @Bean
    @Profile("dev")
    public CommandLineRunner seedWithFakerAndNotifications() {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) {
                // Seed fixed sub-communities

                if (communityRepo.count() > 0) {
                    System.out.println("Database already seeded with communities.");
                    return;
                }

                seedFixedCommunities();

                // Seed random communities
                // seedRandomCommunities();
            }
        };
    }

    @Transactional
    private void seedFixedCommunities() {

        // an “admin” user to own the communities
        UUID adminId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        // ————— Community #1: Java Backend —————
        // 1) sub-topics
        UUID subtopic1Id = UUID.fromString("22222222-2222-2222-2222-111111111111");
        SubTopic springBoot = SubTopic.builder()
                .id(subtopic1Id)
                .name("Spring Boot")
                .build();

        subTopicRepository.save(springBoot);

        SubTopic hibernate  = subTopicService.addSubTopic("Hibernate");
        List<UUID> javaSubIds = Arrays.asList(
                springBoot.getId(),
                hibernate.getId()
        );

//        System.out.println("Java subtopic IDs: " + javaSubIds);

        UUID topic1Id   = UUID.fromString("88888888-8888-8888-8888-888888888888");

        // 2) topic
        Topic javaTopic = Topic.builder()
                .id(topic1Id)
                .name("Java Backend")
                .subtopicIds(javaSubIds)
                .build();
        javaTopic = topicService.createTopic(javaTopic);

//        System.out.println("Java topic ID: " + javaTopic.getId());

        UUID fixedJavaId   = UUID.fromString("44444444-4444-4444-4444-444444444444");


        Community javaCommunity = Community.builder()
                .id(fixedJavaId)                         // <— hard-code the ID
                .name("Java Developers")
                .topicId(topic1Id)
                .description("A place to discuss all things Java backend…")
                .createdBy(adminId)
                .build();


        // add charlie as a member
        UUID charlie = UUID.fromString("55555555-5555-5555-5555-555555555555");

        javaCommunity.getMemberIds().add(charlie);
        javaCommunity.getModeratorIds().add(adminId);
        communityRepo.save(javaCommunity);

        // Add the 6 fixed Java thread UUIDs
        javaCommunity.getThreadIds().addAll(List.of(
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
                UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"),
                UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"),
                UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee"),
                UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff")
        ));

        communityRepo.save(javaCommunity);

//        // 4) add members
//        UUID userA = UUID.randomUUID();
//        UUID userB = UUID.randomUUID();
//        UUID userC = UUID.randomUUID();
//        communityService.addMember(javaCommunity.getId(), userA);
//        communityService.addMember(javaCommunity.getId(), userB);
//        communityService.addMember(javaCommunity.getId(), userC);
//
//        // 5) add threads
//        UUID thread1 = UUID.randomUUID();
//        UUID thread2 = UUID.randomUUID();
//        communityService.addThread(javaCommunity.getId(), thread1);
//        communityService.addThread(javaCommunity.getId(), thread2);
//
//        // 6) ban one member
//        communityService.banUser(javaCommunity.getId(), userC);


        // Community 2: Frontend Frameworks
        SubTopic react = subTopicService.addSubTopic("React");
        SubTopic vue   = subTopicService.addSubTopic("Vue.js");
        List<UUID> frontSubIds = Arrays.asList(
                react.getId(),
                vue.getId()
        );

        UUID topic2Id   = UUID.fromString("99999999-9999-9999-9999-999999999999");

        Topic frontTopic = Topic.builder()
                .id(topic2Id)
                .name("Frontend Frameworks")
                .subtopicIds(frontSubIds)
                .build();
        frontTopic = topicService.createTopic(frontTopic);

        UUID fixedFrontId  = UUID.fromString("33333333-3333-3333-3333-333333333333");

        Community frontCommunity = Community.builder()
                .id(fixedFrontId)                         // <— hard-code the ID
                .name("Frontend Developers")
                .topicId(topic2Id)
                .description("Share tips and tricks for React, Vue, Angular and friends.")
                .createdBy(adminId)
                .build();


        // add bob as a member
        UUID bob = UUID.fromString("22222222-2222-2222-2222-222222222222");

        frontCommunity.getMemberIds().add(bob);
        frontCommunity.getModeratorIds().add(adminId);
        communityRepo.save(frontCommunity);

        // Add the 6 fixed Frontend thread UUIDs
        frontCommunity.getThreadIds().addAll(List.of(
                UUID.fromString("ffffffff-ffff-ffff-ffff-aaaaaaaaaaaa"),
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"),
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac"),
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaad"),
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaae"),
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaf")
        ));

        communityRepo.save(frontCommunity);

//        UUID userX = UUID.randomUUID();
//        UUID userY = UUID.randomUUID();
//        UUID userZ = UUID.randomUUID();
//        communityService.addMember(frontCommunity.getId(), userX);
//        communityService.addMember(frontCommunity.getId(), userY);
//        communityService.addMember(frontCommunity.getId(), userZ);
//
//        UUID thread3 = UUID.randomUUID();
//        UUID thread4 = UUID.randomUUID();
//        communityService.addThread(frontCommunity.getId(), thread3);
//        communityService.addThread(frontCommunity.getId(), thread4);
//
//        communityService.banUser(frontCommunity.getId(), userZ);
    }

}
