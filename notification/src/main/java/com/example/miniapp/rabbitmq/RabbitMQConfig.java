package com.example.miniapp.rabbitmq;

import com.example.miniapp.models.dto.NotificationRequest;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper.TypePrecedence;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQConfig {
    public static final String COMMUNITY_EXCHANGE            = "community_exchange";
    public static final String THREAD_EXCHANGE               = "thread_exchange";
    public static final String USER_EXCHANGE                 = "user_exchange";

    public static final String MULTI_QUEUE            = "notification.requests.queue";

    public static final String COMMUNITY_NOTIFICATION_KEY    = "community.notification";
    public static final String THREAD_NOTIFICATION_KEY       = "thread.notification";
    public static final String USER_NOTIFICATION_KEY         = "user.notification";

    // 1) Single queue for all notifications
    @Bean
    public Queue notificationQueue() {
        return new Queue(MULTI_QUEUE, true);
    }

    // 2) Declare all three exchanges
    @Bean
    public TopicExchange communityExchange() {
        return new TopicExchange(COMMUNITY_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange threadExchange() {
        return new TopicExchange(THREAD_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE, true, false);
    }

    // 3) Bind the one queue to each exchange/routing key
    @Bean
    public Binding bindCommunityNotifications(Queue notificationQueue,
                                              TopicExchange communityExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(communityExchange)
                .with(COMMUNITY_NOTIFICATION_KEY);
    }

    @Bean
    public Binding bindThreadNotifications(Queue notificationQueue,
                                           TopicExchange threadExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(threadExchange)
                .with(THREAD_NOTIFICATION_KEY);
    }

    @Bean
    public Binding bindUserNotifications(Queue notificationQueue,
                                         TopicExchange userExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(userExchange)
                .with(USER_NOTIFICATION_KEY);
    }

    // 4) JSON converter with simple type‐ID mapping
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setIdClassMapping(Map.of(
                "NotificationRequest", NotificationRequest.class
        ));
        typeMapper.setTypePrecedence(TypePrecedence.TYPE_ID);
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }

    // 5) Ensure @RabbitListener uses our converter
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            Jackson2JsonMessageConverter converter
    ) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(converter);
        return factory;
    }
}
