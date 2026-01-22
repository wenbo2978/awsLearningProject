package com.aws_practice.rabbitmq.order;

import com.aws_practice.dto.OrderPlacedDomainEvent;
import com.aws_practice.dto.OrderPlacedEvent;
import com.aws_practice.rabbitmq.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderPlacedConsumer {

    @RabbitListener(queues = RabbitConfig.ORDER_PLACED_QUEUE)
    public void onMessage(OrderPlacedEvent event) {
        // This method is called when a message arrives in order.placed.q
        System.out.println("Order placed: " + event);

        // do side effects:
        // emailService.sendConfirmation(event.orderId(), event.userId());
        // analyticsService.trackOrderPlaced(event);
    }
}
