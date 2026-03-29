package ru.top.kitchenmanager.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.kitchenmanager.dto.OrderDto;
import ru.top.kitchenmanager.model.Dish;
import ru.top.kitchenmanager.model.Order;
import ru.top.kitchenmanager.model.OrderItem;
import ru.top.kitchenmanager.model.OrderStatus;
import ru.top.kitchenmanager.repository.DishRepository;
import ru.top.kitchenmanager.repository.OrderRepository;
import ru.top.kitchenmanager.service.OrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DishRepository dishRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Long createOrder(OrderDto orderDto, Map<Long, Integer> cart) {
        Order order = new Order();
        order.setClientName(orderDto.getClientName());
        order.setClientPhone(orderDto.getClientPhone());
        order.setAddress(orderDto.getAddress());
        order.setPickup(orderDto.isPickup());
        order.setStatus(OrderStatus.NEW);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Dish dish = dishRepository.findById(entry.getKey()).orElse(null);
            if (dish != null && dish.getAvailable()) {
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setDish(dish);
                item.setQuantity(entry.getValue());
                item.setPrice(dish.getPrice());
                orderItems.add(item);

                BigDecimal itemTotal = dish.getPrice()
                        .multiply(BigDecimal.valueOf(entry.getValue()));
                total = total.add(itemTotal);
            }
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);
        return savedOrder.getId();
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setStatus(newStatus);
            order = orderRepository.save(order);
        }
        return order;
    }
    @Override
    public long countByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    @Override
    public List<Order> getRecentOrders(int limit) {
        return orderRepository.findRecentOrders(limit);
    }

    @Override
    public Map<OrderStatus, Long> getOrderStatistics() {
        Map<OrderStatus, Long> stats = new HashMap<>();
        for (OrderStatus status : OrderStatus.values()) {
            stats.put(status, orderRepository.countByStatus(status));
        }
        return stats;
    }
    @Override
    public List<Order> getOrdersForAdmin() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersForCook() {
        return orderRepository.findByStatusIn(List.of(
                OrderStatus.NEW,
                OrderStatus.CONFIRMED,
                OrderStatus.COOKING
        ));
    }

    @Override
    public List<Order> getOrdersForCourier() {
        return orderRepository.findByStatusIn(List.of(
                OrderStatus.READY,
                OrderStatus.DELIVERING
        ));
    }
    @Override
    public List<Order> getOrdersByClientPhone(String phone) {
        return orderRepository.findByClientPhone(phone);
    }
}