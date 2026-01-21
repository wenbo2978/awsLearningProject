package com.aws_practice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderPlacedDomainEvent(
        Long orderId,
        Long userId,
        BigDecimal totalAmount,
        LocalDate placedAt
) {}
