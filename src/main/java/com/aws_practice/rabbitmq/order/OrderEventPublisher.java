package com.aws_practice.rabbitmq.order;

import com.aws_practice.dto.OrderPlacedDomainEvent;
import com.aws_practice.rabbitmq.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishOrderPlaced(OrderPlacedDomainEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ORDER_PLACED_KEY,
                event
        );
    }
}
