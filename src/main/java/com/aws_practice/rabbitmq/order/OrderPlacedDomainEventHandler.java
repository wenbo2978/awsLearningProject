package com.aws_practice.rabbitmq.order;

import com.aws_practice.dto.OrderPlacedDomainEvent;
import com.aws_practice.dto.OrderPlacedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;

@Component
public class OrderPlacedDomainEventHandler {
    private final OrderEventPublisher rabbitPublisher;

    public OrderPlacedDomainEventHandler(OrderEventPublisher rabbitPublisher) {
        this.rabbitPublisher = rabbitPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderPlacedDomainEvent e) {
        rabbitPublisher.publishOrderPlaced(
                new OrderPlacedEvent(
                        e.orderId(),
                        e.userId(),
                        e.totalAmount(),
                        Instant.now()
                )
        );
    }
}
