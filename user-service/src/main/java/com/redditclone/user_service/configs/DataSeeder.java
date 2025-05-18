package com.redditclone.user_service.configs;

import com.github.javafaker.Faker;
import com.redditclone.user_service.models.Block;
import com.redditclone.user_service.models.User;
import com.redditclone.user_service.repositories.BlockRepository;
import com.redditclone.user_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Seeds the UserService database with both fixed notification users,
 * random users, and blocks for dev environment.
 */
@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final PasswordEncoder passwordEncoder;

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
                seedFixedNotificationUsers();
                seedTestApiUsers();
                seedFakeUsers();
                seedTestBlockRelation();
                seedBlocks();
            }
        };
    }

    private void seedFixedNotificationUsers() {
        // Alice
        String ALICE_USERNAME = "alice";
        String ALICE_EMAIL = "alice@example.com";
        User alice = User.builder()
                .username(ALICE_USERNAME)
                .password(passwordEncoder.encode("password"))
                .email(ALICE_EMAIL)
                .fullName("Alice Notification")
                .createdAt(Instant.now())
                .activated(true)
                .lastLogin(Instant.now())
                .build();
        userRepository.save(alice);
        System.out.println("[DataSeeder] Created user: alice");

        // Bob
        String BOB_USERNAME = "bob";
        String BOB_EMAIL = "bob@example.com";
        User bob = User.builder()
                .username(BOB_USERNAME)
                .password(passwordEncoder.encode("password"))
                .email(BOB_EMAIL)
                .fullName("Bob Notification")
                .createdAt(Instant.now())
                .activated(true)
                .lastLogin(Instant.now())
                .build();
        userRepository.save(bob);
        System.out.println("[DataSeeder] Created user: bob");
    }

    private void seedFakeUsers() {
        long totalUsers = userRepository.count();
        if (totalUsers < 52) { // two fixed + 50 fake
            Faker faker = new Faker();
            IntStream.rangeClosed(1, 50).forEach(i -> {
                String fn = faker.name().firstName();
                String ln = faker.name().lastName();
                User u = User.builder()
                        .username((fn.charAt(0) + ln).toLowerCase())
                        .password(passwordEncoder.encode("password"))
                        .email(fn.toLowerCase() + "." + ln.toLowerCase() + "@example.com")
                        .fullName(fn + " " + ln)
                        .createdAt(Instant.now().minusSeconds(faker.number().numberBetween(0, 86_400)))
                        .activated(faker.bool().bool())
                        .lastLogin(Instant.now().minusSeconds(faker.number().numberBetween(0, 86_400)))
                        .build();
                userRepository.save(u);
            });
            System.out.println("[DataSeeder] Seeded 50 fake users");
        } else {
            System.out.println("[DataSeeder] Fake users already seeded");
        }
    }

    private void seedBlocks() {
        long blockCount = blockRepository.count();
        System.out.println("Seeding blocks... " + blockCount);
        if (blockCount == 0) {
            Faker faker = new Faker();
            List<User> activeUsers = userRepository.findByActivatedTrue();
            activeUsers.forEach(blocker -> {
                int blocksForUser = faker.number().numberBetween(0, 4);
                List<User> candidates = new ArrayList<>(activeUsers);
                candidates.removeIf(u -> u.getId().equals(blocker.getId()));
                Collections.shuffle(candidates);
                int limit = Math.min(blocksForUser, candidates.size());
                for (int i = 0; i < limit; i++) {
                    User blocked = candidates.get(i);
                    Block b = Block.builder()
                            .blocker(blocker)
                            .blocked(blocked)
                            .build();
                    blockRepository.save(b);
                }
            });
            System.out.println("[DataSeeder] Seeded blocks for activated users");
        } else {
            System.out.println("[DataSeeder] Blocks already seeded");
        }
    }
    private void seedTestApiUsers() {
        if (userRepository.findByUsername("apitest_active").isEmpty()) {
            User activeUser = User.builder()
                    .username("apitest_active")
                    .password(passwordEncoder.encode("pass123"))
                    .email("apitest_active@example.com")
                    .fullName("Active API Test")
                    .createdAt(Instant.now())
                    .activated(true)
                    .lastLogin(Instant.now())
                    .build();
            userRepository.save(activeUser);
            System.out.println("[DataSeeder] Created user: apitest_active");
        }

        if (userRepository.findByUsername("apitest_blocked").isEmpty()) {
            User blockedUser = User.builder()
                    .username("apitest_blocked")
                    .password(passwordEncoder.encode("pass123"))
                    .email("apitest_blocked@example.com")
                    .fullName("Blocked API Test")
                    .createdAt(Instant.now())
                    .activated(true)
                    .lastLogin(Instant.now())
                    .build();
            userRepository.save(blockedUser);
            System.out.println("[DataSeeder] Created user: apitest_blocked");
        }
        if (userRepository.findByUsername("apitest_blockedE2E").isEmpty()) {
            User blockedUser = User.builder()
                    .username("apitest_blockedE2E")
                    .password(passwordEncoder.encode("pass123"))
                    .email("apitest_blockedE2E@example.com")
                    .fullName("Blocked API Test")
                    .createdAt(Instant.now())
                    .activated(true)
                    .lastLogin(Instant.now())
                    .build();
            userRepository.save(blockedUser);
            System.out.println("[DataSeeder] Created user: apitest_blockedE2E");
        }
    }

    private void seedTestBlockRelation() {
        User blocker = userRepository.findByUsername("apitest_active").orElse(null);
        User blocked = userRepository.findByUsername("apitest_blocked").orElse(null);

        if (blocker != null && blocked != null) {
            Block b = Block.builder()
                    .blocker(blocker)
                    .blocked(blocked)
                    .build();
            blockRepository.save(b);
            System.out.println("[DataSeeder] Blocked apitest_blocked by apitest_active");
        }
    }

}
