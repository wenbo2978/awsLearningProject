package com.aws_practice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummaryDto(
        Long orderId,
        String userEmail,
        BigDecimal totalAmount,
        Integer itemCount,
        LocalDateTime orderDate
) {
}
