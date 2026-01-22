package com.aws_practice.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderPlacedEvent(
        Long orderId,
        Long userId,
        BigDecimal totalAmount,
        Instant placedAt
) {
}
