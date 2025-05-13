package com.example.moderator.rabbitmq;

import com.example.moderator.dto.BanRequest;
import com.example.moderator.dto.DeleteCommentRequest;
import com.example.moderator.dto.ReportRequest;
import com.example.moderator.dto.UnbanRequest;
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
    public static final String THREAD_EXCHANGE = "thread_exchange";

    public static final String REPORT_QUEUE = "report_queue";
    public static final String REPORT_REQUEST_ROUTING_KEY = "thread.reportRequest";

    public static final String DELETE_COMMENT_REQUEST_ROUTING_KEY = "moderator.deleteCommentRequest";

    // Community Service
    public static final String COMMUNITY_EXCHANGE = "community_exchange";
    public static final String COMMUNITY_BAN_ROUTING    = "community.banUser";
    public static final String COMMUNITY_UNBAN_ROUTING  = "community.unbanUser";

    @Bean
    public Queue reportQueue() {
        return new Queue(REPORT_QUEUE);
    }

    @Bean
    public TopicExchange threadExchange() {
        return new TopicExchange(THREAD_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange communityExchange() {
        return new TopicExchange(COMMUNITY_EXCHANGE, true, false);
    }

    @Bean
    public Binding reportBinding(Queue reportQueue, TopicExchange threadExchange) {
        return BindingBuilder.bind(reportQueue).to(threadExchange).with(REPORT_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("ReportRequest", ReportRequest.class);
        idClassMapping.put("DeleteCommentRequest", DeleteCommentRequest.class);
        idClassMapping.put("BanRequest", BanRequest.class);
        idClassMapping.put("UnbanRequest", UnbanRequest.class);

        typeMapper.setIdClassMapping(idClassMapping);
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            Jackson2JsonMessageConverter converter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(converter);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(converter);
        return tpl;
    }
}
