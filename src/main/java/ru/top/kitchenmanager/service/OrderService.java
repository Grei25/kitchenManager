package ru.top.kitchenmanager.service;
import ru.top.kitchenmanager.dto.OrderDto;
import ru.top.kitchenmanager.model.Order;
import ru.top.kitchenmanager.model.OrderStatus;

import java.util.List;
import java.util.Map;

public interface OrderService {
    List<Order> getAllOrders();
    List<Order> getOrdersByStatus(OrderStatus status);
    Order getOrderById(Long id);
    Long createOrder(OrderDto orderDto, Map<Long, Integer> cart);
    Order updateOrderStatus(Long orderId, OrderStatus newStatus);
    Order acceptOrder(Long orderId);
    Order completeOrder(Long orderId);
    List<Order> getOrdersByClientPhone(String phone);
    long countByStatus(OrderStatus status);
    List<Order> getRecentOrders(int limit);
    Map<OrderStatus, Long> getOrderStatistics();
    List<Order> getOrdersForAdmin();      // все заказы
    List<Order> getOrdersForCook();       // заказы со статусами NEW, CONFIRMED, COOKING
    List<Order> getOrdersForCourier();    // заказы со статусами READY, DELIVERING
}