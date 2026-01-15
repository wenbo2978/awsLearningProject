package com.aws_practice.services.order;

import com.aws_practice.dto.OrderDto;
import com.aws_practice.models.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
    OrderDto convertToDto(Order order);
}
