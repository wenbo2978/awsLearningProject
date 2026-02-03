package com.aws_practice.services.order;

import com.aws_practice.dto.OrderDto;
import com.aws_practice.dto.OrderPlacedDomainEvent;
import com.aws_practice.dto.OrderSummaryDto;
import com.aws_practice.enums.OrderStatus;
import com.aws_practice.exceptions.ResourceNotFoundException;
import com.aws_practice.mapper.OrderQueryMapper;
import com.aws_practice.models.Cart;
import com.aws_practice.models.Order;
import com.aws_practice.models.OrderItem;
import com.aws_practice.models.Product;
import com.aws_practice.repositories.OrderRepository;
import com.aws_practice.repositories.ProductRepository;
import com.aws_practice.services.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher appEventPublisher;
    private final OrderQueryMapper orderQueryMapper;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart   = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());

        /*
        * Publish ONLY after successful commit
        * */

        OrderPlacedDomainEvent event = new OrderPlacedDomainEvent(
                savedOrder.getOrderId(),
                userId,
                savedOrder.getTotalAmount()
        );

        appEventPublisher.publishEvent(event);

        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return  order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return  cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return  new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice());
        }).toList();

    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return  orderItemList
                .stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this :: convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return  orders.stream().map(this :: convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public List<OrderSummaryDto> getOrderSummaryByUserId(Long userId) {
        return orderQueryMapper.findOrderSummaryByUserId(userId);
    }
}
