package com.example.miniapp.config;

import com.example.miniapp.services.strategy.DeliveryStrategy;
import com.example.miniapp.services.strategy.impl.EmailStrategy;
import com.example.miniapp.services.strategy.impl.PushStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class StrategyConfig {
    @Bean
    @ConditionalOnProperty(name="notification.strategy", havingValue="push")
    public DeliveryStrategy pushStrategy() {
        return new PushStrategy();
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name="notification.strategy", havingValue="email")
    public DeliveryStrategy emailStrategy() {
        return new EmailStrategy();
    }
}