package com.redditclone.communities_service.rabbitmq;

import com.redditclone.communities_service.dto.BanRequest;
import com.redditclone.communities_service.dto.NotificationRequest;
import com.redditclone.communities_service.dto.UnbanRequest;
import com.redditclone.communities_service.models.MemberDTO;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "community_exchange";
    public static final String MOD_COMMANDS_QUEUE    = "community.moderator.commands";
    public static final String ROUTING_BAN           = "community.banUser";
    public static final String ROUTING_UNBAN         = "community.unbanUser";
    public static final String ROUTING_NOTIFICATION  = "community.notification";


    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE);
    }


    @Bean
    public Queue moderatorCommandsQueue() {
        return new Queue(MOD_COMMANDS_QUEUE, true);
    }

    @Bean
    public Binding bindingBan(Queue moderatorCommandsQueue, TopicExchange communityExchange) {
        return BindingBuilder
                .bind(moderatorCommandsQueue)
                .to(communityExchange)
                .with(ROUTING_BAN);
    }

    @Bean
    public Binding bindingUnban(Queue moderatorCommandsQueue, TopicExchange communityExchange) {
        return BindingBuilder
                .bind(moderatorCommandsQueue)
                .to(communityExchange)
                .with(ROUTING_UNBAN);
    }

    /** Convert your POJOs to JSON when sending */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        // 1. Create a type-mapper and tell it which simple IDs map to which classes:
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idToClass = new HashMap<>();
        idToClass.put("MemberDTO",    MemberDTO.class);
        idToClass.put("BanRequest", BanRequest.class);
        idToClass.put("UnbanRequest", UnbanRequest.class);
        idToClass.put("NotificationRequest", NotificationRequest.class);
        // …add any other event/DTO types you need
        typeMapper.setIdClassMapping(idToClass);

        // 2. Tell it to use those IDs instead of the FQN
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);

        converter.setJavaTypeMapper(typeMapper);
        return converter;

    }

    /** Expose a RabbitTemplate that uses JSON conversion */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(converter);
        return tpl;
    }

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