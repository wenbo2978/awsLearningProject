package com.aws_practice.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "ecommerce.events";
    public static final String ORDER_PLACED_QUEUE = "order.placed.q";
    public static final String ORDER_PLACED_KEY = "order.placed";

    @Bean
    public TopicExchange ecommerceExchange(){
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue orderPlacedQueue() {
        return QueueBuilder.durable(ORDER_PLACED_QUEUE).build();
    }

    @Bean
    public Binding orderPlacedBinding(Queue orderPlacedQueue, TopicExchange ecommerceExchange) {
        return BindingBuilder.bind(orderPlacedQueue).to(ecommerceExchange).with(ORDER_PLACED_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jacksonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(converter);
        return template;
    }
}
