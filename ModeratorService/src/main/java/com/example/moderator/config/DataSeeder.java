package com.example.moderator.config;

import com.example.moderator.model.Moderator;
import com.example.moderator.repository.ModeratorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Configuration
public class DataSeeder {
    private final ModeratorRepository moderatorRepository;

    public DataSeeder(ModeratorRepository moderatorRepository) {
        this.moderatorRepository = moderatorRepository;
    }

    @Bean
    public CommandLineRunner seedWithFakerAndNotifications() {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) {
                seedFixedModerators();
            }
        };
    }

    private void seedFixedModerators() {

        String moderatorId = "11111111-1111-1111-1111-111111111111"; // alice
        String community1Id = "44444444-4444-4444-4444-444444444444"; // java
        String community2Id = "33333333-3333-3333-3333-333333333333"; // front-end

        // Create a moderator for the community
        Moderator moderator = new Moderator(UUID.fromString(moderatorId),
                UUID.fromString(community1Id));

        // save moderator with community 1
        moderatorRepository.save(moderator);

        // Create a moderator for the community 2
        Moderator moderator2 = new Moderator(UUID.fromString(moderatorId),
                UUID.fromString(community2Id));
        // save moderator with community 2
        moderatorRepository.save(moderator2);
    }
}
