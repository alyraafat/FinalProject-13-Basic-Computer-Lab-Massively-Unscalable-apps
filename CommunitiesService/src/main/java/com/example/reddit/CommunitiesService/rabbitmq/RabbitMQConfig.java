package com.example.reddit.CommunitiesService.rabbitmq;

import org.springframework.amqp.core.TopicExchange;
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
//    public static final String COMMUNITY_QUEUE = "community_queue";
    public static final String EXCHANGE = "community_exchange";
//    public static final String COMMUNITY_ROUTING = "community_routing";

//    @Bean
//    public Queue queue(){
//        return new Queue(COMMUNITY_QUEUE);
//    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE);
    }

//    @Bean
//    public Binding binding(Queue queue, TopicExchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with(COMMUNITY_ROUTING);
//    }

    /** Convert your POJOs to JSON when sending */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        // 1. Create a type-mapper and tell it which simple IDs map to which classes:
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idToClass = new HashMap<>();
        idToClass.put("MemberDTO",    com.example.reddit.CommunitiesService.models.MemberDTO.class);

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


}