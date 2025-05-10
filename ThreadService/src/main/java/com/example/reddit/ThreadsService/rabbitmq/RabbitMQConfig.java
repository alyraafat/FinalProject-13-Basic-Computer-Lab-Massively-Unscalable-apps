package com.example.reddit.ThreadsService.rabbitmq;

import com.example.reddit.ThreadsService.dto.DeleteCommentRequest;
import com.example.reddit.ThreadsService.dto.ReportRequest;
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

    public static final String DELETE_COMMENT_QUEUE = "delete_comment_queue";
    public static final String DELETE_COMMENT_REQUEST_ROUTING_KEY = "moderator.deleteCommentRequest";

    public static final String REPORT_REQUEST_ROUTING_KEY = "thread.reportRequest";

    @Bean
    public Queue deleteCommentQueue() {
        return new Queue(DELETE_COMMENT_QUEUE);
    }

    @Bean
    public TopicExchange threadExchange() {
        return new TopicExchange(THREAD_EXCHANGE, true, false);
    }

    @Bean
    public Binding deleteCommentBinding(Queue deleteCommentQueue, TopicExchange threadExchange) {
        return BindingBuilder.bind(deleteCommentQueue).to(threadExchange).with(DELETE_COMMENT_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("ReportRequest", ReportRequest.class);
        idClassMapping.put("DeleteCommentRequest", DeleteCommentRequest.class);

        typeMapper.setIdClassMapping(idClassMapping);
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(converter);
        return tpl;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(converter);
        return factory;
    }
}
