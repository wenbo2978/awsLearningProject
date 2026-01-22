package com.aws_practice.rabbitmq.order;

import com.aws_practice.dto.OrderPlacedDomainEvent;
import com.aws_practice.dto.OrderPlacedEvent;
import com.aws_practice.rabbitmq.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishOrderPlaced(OrderPlacedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ORDER_PLACED_KEY,
                event
        );
    }
}
