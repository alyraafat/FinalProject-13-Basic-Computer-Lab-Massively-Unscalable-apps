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
import java.util.UUID;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Profile("dev")
    CommandLineRunner seedWithFaker() {
        return args -> {
            // 1) Seed users if none exist
            if (userRepository.count() == 0) {
                Faker faker = new Faker();
                IntStream.rangeClosed(1, 50).forEach(i -> {
                    String fn = faker.name().firstName();
                    String ln = faker.name().lastName();
                    User u = User.builder()
                            // no .id() so JPA will INSERT
                            .username((fn.charAt(0) + ln).toLowerCase())
                            .password(passwordEncoder.encode(faker.internet().password(8, 16)))
                            .email(fn.toLowerCase() + "." + ln.toLowerCase() + "@example.com")
                            .fullName(fn + " " + ln)
                            .createdAt(Instant.now().minusSeconds(faker.number().numberBetween(0, 86_400)))
                            .activated(faker.bool().bool())
                            .lastLogin(Instant.now().minusSeconds(faker.number().numberBetween(0, 86_400)))
                            .build();
                    userRepository.save(u);
                });
                System.out.println("✅ Seeded 50 fake users");
            }

            System.out.println("Seeding blocks... "+ blockRepository.count());
            if (blockRepository.count() == 0) {
                Faker faker = new Faker();
                List<User> activeUsers = userRepository.findByActivatedTrue();

                activeUsers.forEach(blocker -> {
                    // 0–3 blocks for this blocker
                    int blocksForUser = faker.number().numberBetween(0, 4);

                    // build a mutable list of everyone except the blocker
                    List<User> candidates = new ArrayList<>(activeUsers);
                    candidates.removeIf(u -> u.getId().equals(blocker.getId()));

                    // shuffle so we’ll pick a random subset
                    Collections.shuffle(candidates);

                    // pick at most blocksForUser distinct targets
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

                System.out.println("✅ Seeded blocks for activated users (no duplicates)");
            }
        };
    }
}
