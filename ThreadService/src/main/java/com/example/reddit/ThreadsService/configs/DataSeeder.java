package com.example.reddit.ThreadsService.configs;

import com.example.reddit.ThreadsService.models.ActionType;
import com.example.reddit.ThreadsService.models.LogReflectionFactory;
import com.example.reddit.ThreadsService.repositories.ThreadRepository;
import com.example.reddit.ThreadsService.services.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import com.example.reddit.ThreadsService.models.Thread;



import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;


@Configuration
public class DataSeeder {

    @Autowired
    private final ThreadRepository threadRepository;

    @Autowired
    private final LogReflectionFactory logReflectionFactory;



//    @Autowired
//    private final ModeratorClient moderatorClient;

    public DataSeeder(ThreadRepository threadRepository, LogReflectionFactory logReflectionFactory) {
        this.threadRepository = threadRepository;
        this.logReflectionFactory = logReflectionFactory;
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
                seedFixedThreads();

                // Seed random communities
                // seedRandomCommunities();
            }
        };
    }

    @Transactional
    public void seedFixedThreads() {
// fixed community IDs (must match what you used in CommunitiesService)
        UUID javaCommId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID frontCommId = UUID.fromString("33333333-3333-3333-3333-333333333333");

        // pick some authors
        UUID bob = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID charlie = UUID.fromString("55555555-5555-5555-5555-555555555555");

        UUID topic1 = UUID.fromString("88888888-8888-8888-8888-888888888888");
        UUID topic2 = UUID.fromString("99999999-9999-9999-9999-999999999999");

        System.out.print("Seeding1 fixed threads...");
        Thread thread1 = new Thread.Builder()
                .id(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .title("Welcome to Java Community")
                .content("Kickoff discussion for Java devs!")
                .authorId(charlie)
                .communityId(javaCommId)
                .topic(topic1)
                .createdAt(LocalDateTime.now())
                .upVotes(1)
                .downVotes(0)
                .comments(List.of())
                .build();
        System.out.print("Seeding2 fixed threads...");

        logReflectionFactory.createLog(ActionType.POST, charlie, UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        threadRepository.save(thread1);

        System.out.print("Seeding3 fixed threads...");


        Thread thread2 = new Thread.Builder()
                .id(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))
                .title("Spring Boot Tips")
                .content("Share your favorite @Annotations.")
                .authorId(charlie)
                .communityId(javaCommId)
                .topic(topic1)
                .createdAt(LocalDateTime.now())
                .upVotes(2)
                .downVotes(0)
                .comments(List.of())
                .build();
        logReflectionFactory.createLog(ActionType.POST, charlie, UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
        threadRepository.save(thread2);


        Thread thread3 = new Thread.Builder()
                .id(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"))
                .title("Hibernate Performance Tricks")
                .content("How do you optimize lazy vs eager fetching?")
                .authorId(charlie)
                .communityId(javaCommId)
                .topic(topic1)
                .createdAt(LocalDateTime.now())
                .upVotes(3)
                .downVotes(0)
                .comments(List.of())
                .build();
        logReflectionFactory.createLog(ActionType.POST, charlie, UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));
        threadRepository.save(thread3);



        Thread thread4 = new Thread.Builder()
                .id(UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"))
                .title("Java Records in DTO mapping")
                .content("Using MapStruct + records?")
                .authorId(charlie)
                .communityId(javaCommId)
                .topic(topic1)
                .createdAt(LocalDateTime.now())
                .upVotes(4)
                .downVotes(0)
                .comments(List.of())
                .build();
        logReflectionFactory.createLog(ActionType.POST, charlie, UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"));
        threadRepository.save(thread4);


        Thread thread5 = new Thread.Builder()
                .id(UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee"))
                .title("JPA Criteria API is painful")
                .content("Has anyone replaced it with QueryDSL?")
                .authorId(charlie)
                .communityId(javaCommId)
                .topic(topic1)
                .createdAt(LocalDateTime.now())
                .upVotes(5)
                .downVotes(0)
                .comments(List.of())
                .build();
        logReflectionFactory.createLog(ActionType.POST, charlie, UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee"));
        threadRepository.save(thread5);

        Thread thread6 = new Thread.Builder()
                .id(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff"))
                .title("Lombok or no Lombok?")
                .content("What’s your stance in 2025?")
                .authorId(charlie)
                .communityId(javaCommId)
                .topic(topic1)
                .createdAt(LocalDateTime.now())
                .upVotes(6)
                .downVotes(0)
                .comments(List.of())
                .build();
        logReflectionFactory.createLog(ActionType.POST, charlie, UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff"));
        threadRepository.save(thread6);

        Thread thread7 = new Thread.Builder()
                .id(UUID.fromString("ffffffff-ffff-ffff-ffff-aaaaaaaaaaaa"))
                .title("React 19 Predictions")
                .content("What's coming next in React?")
                .authorId(bob)
                .communityId(frontCommId)
                .topic(topic2)
                .createdAt(LocalDateTime.now())
                .upVotes(100)
                .downVotes(80)
                .comments(List.of())
                .build();
        logReflectionFactory.createLog(ActionType.POST, bob, UUID.fromString("ffffffff-ffff-ffff-ffff-aaaaaaaaaaaa"));
        threadRepository.save(thread7);

        Thread thread8 = new Thread.Builder()
                .id(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .title("Tailwind vs Bootstrap")
                .content("What’s your go-to styling approach?")
                .authorId(bob)
                .communityId(frontCommId)
                .topic(topic2)
                .createdAt(LocalDateTime.of(2024, 12, 25, 10, 30))
                .upVotes(50)
                .downVotes(30)
                .comments(List.of())
                .build();
        logReflectionFactory.createLog(ActionType.POST, bob, UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"));
        threadRepository.save(thread8);


        Thread thread9 = new Thread.Builder()
                .id(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac"))
                .title("State Management in 2025")
                .content("Redux, Zustand, Jotai — what wins?")
                .authorId(bob)
                .communityId(frontCommId)
                .topic(topic2)
                .createdAt(LocalDateTime.of(2025, 12, 25, 10, 30))
                .upVotes(20)
                .downVotes(1)
                .comments(List.of())
                .build();
        logReflectionFactory.createLog(ActionType.POST, bob, UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac"));
        threadRepository.save(thread9);


        Thread thread10 = new Thread.Builder()
                .id(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaad"))
                .title("Using Next.js App Router?")
                .content("Share your migration experience.")
                .authorId(bob)
                .communityId(frontCommId)
                .topic(topic2)
                .createdAt(LocalDateTime.of(2024, 10, 25, 10, 30))
                .upVotes(0)
                .downVotes(0)
                .comments(List.of())
                .build();
        logReflectionFactory.createLog(ActionType.POST, bob, UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaad"));
        threadRepository.save(thread10);


        Thread thread11 = new Thread.Builder()
                .id(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaae"))
                .title("CSS Modules or Global Styles?")
                .content("What's more manageable in large teams?")
                .authorId(bob)
                .communityId(frontCommId)
                .topic(topic2)
                .createdAt(LocalDateTime.of(2024, 11, 25, 10, 30))
                .upVotes(9)
                .downVotes(8)
                .comments(List.of())
                .build();
        logReflectionFactory.createLog(ActionType.POST, bob, UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaae"));
        threadRepository.save(thread11);


        Thread thread12 = new Thread.Builder()
                .id(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaf"))
                .title("Dark Mode by Default?")
                .content("Should your app start with dark theme?")
                .authorId(bob)
                .communityId(frontCommId)
                .topic(topic2)
                .createdAt(LocalDateTime.of(2024, 12, 25, 10, 30))
                .upVotes(5)
                .downVotes(4)
                .comments(List.of())
                .build();
        logReflectionFactory.createLog(ActionType.POST, bob, UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaf"));
        threadRepository.save(thread12);
    }
}
