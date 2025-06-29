package com.redditclone.user_service.configs;

import com.redditclone.user_service.models.User;
import com.redditclone.user_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
@Slf4j
public class RedisBloomFilterConfig {

    // A unique key for our bloom filter in Redis
    public static final String USER_CREDENTIALS_BF_KEY = "userCredentialsBloomFilter";

    @Bean
    public RBloomFilter<String> userCredentialsBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(USER_CREDENTIALS_BF_KEY);

        // Initialize the Bloom Filter with expected insertions and a desired false positive probability.
        // This command is atomic and will only initialize the filter if it doesn't already exist in Redis.
        // All service instances will share this same filter.
        long expectedInsertions = 1_000_000L; // Expected number of users
        double falsePositiveProbability = 0.01; // 1% error rate

        bloomFilter.tryInit(expectedInsertions, falsePositiveProbability);

        return bloomFilter;
    }

    /**
     * This component runs on application startup. It checks if the Bloom filter has been
     * populated before, and if not, it fills it with data from the main database.
     * This ensures the filter is seeded correctly on its very first deployment.
     */
    @Component
    @Order(2)
    @RequiredArgsConstructor
    @Slf4j
    public static class BloomFilterPopulationRunner implements ApplicationRunner {

        private final UserRepository userRepository;
        private final RBloomFilter<String> bloomFilter;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            // We check the estimated insertion count. If it's 0, it means the filter is new and unpopulated.
            if (bloomFilter.getExpectedInsertions() > 0 && bloomFilter.count() == 0) {
                log.info("Bloom filter '{}' is new. Starting population from database...", USER_CREDENTIALS_BF_KEY);
                List<User> users = userRepository.findAll();
                if (!users.isEmpty()) {
                    for (User user : users) {
                        bloomFilter.add(user.getUsername());
                        bloomFilter.add(user.getEmail());
                    }
                }
                log.info("Bloom filter population complete. Total items added: {}", bloomFilter.count());
            } else {
                log.info("Bloom filter '{}' already exists and is populated. Skipping database load.", USER_CREDENTIALS_BF_KEY);
                log.info("Current filter stats - Expected Insertions: {}, Item Count: {}",
                        bloomFilter.getExpectedInsertions(), bloomFilter.count());
            }
        }
    }
}