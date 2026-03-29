package ru.top.kitchenmanager.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.top.kitchenmanager.dto.OrderDto;
import ru.top.kitchenmanager.model.Dish;
import ru.top.kitchenmanager.model.Order;
import ru.top.kitchenmanager.model.OrderStatus;
import ru.top.kitchenmanager.repository.DishRepository;
import ru.top.kitchenmanager.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Dish testDish;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testDish = new Dish();
        testDish.setId(1L);
        testDish.setName("Пицца");
        testDish.setPrice(BigDecimal.valueOf(500));
        testDish.setAvailable(true);

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setClientName("Иван");
        testOrder.setClientPhone("+79991234567");
        testOrder.setStatus(OrderStatus.NEW);
        testOrder.setTotalAmount(BigDecimal.valueOf(500));
    }

    @Test
    void getAllOrders() {
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();
        assertEquals(1, result.size());
        verify(orderRepository).findAll();
    }

    @Test
    void getOrdersByStatus() {
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findByStatus(OrderStatus.NEW)).thenReturn(orders);

        List<Order> result = orderService.getOrdersByStatus(OrderStatus.NEW);
        assertEquals(1, result.size());
        assertEquals(OrderStatus.NEW, result.get(0).getStatus());
    }

    @Test
    void getOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Order result = orderService.getOrderById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getOrderByIdNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        Order result = orderService.getOrderById(999L);
        assertNull(result);
    }

    @Test
    void createOrder() {
        OrderDto dto = new OrderDto();
        dto.setClientName("Иван");
        dto.setClientPhone("+79991234567");
        dto.setAddress("ул. Пушкина");
        dto.setPickup(false);

        Map<Long, Integer> cart = new HashMap<>();
        cart.put(1L, 2);

        when(dishRepository.findById(1L)).thenReturn(Optional.of(testDish));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Long orderId = orderService.createOrder(dto, cart);
        assertEquals(1L, orderId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrderWithPickup() {
        OrderDto dto = new OrderDto();
        dto.setClientName("Иван");
        dto.setClientPhone("+79991234567");
        dto.setPickup(true);

        Map<Long, Integer> cart = new HashMap<>();
        cart.put(1L, 1);

        when(dishRepository.findById(1L)).thenReturn(Optional.of(testDish));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        Long orderId = orderService.createOrder(dto, cart);
        assertEquals(2L, orderId);
    }

    @Test
    void updateOrderStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.updateOrderStatus(1L, OrderStatus.CONFIRMED);
        assertNotNull(result);
        assertEquals(OrderStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void updateOrderStatusNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        Order result = orderService.updateOrderStatus(999L, OrderStatus.CONFIRMED);
        assertNull(result);
    }

    @Test
    void countByStatus() {
        when(orderRepository.countByStatus(OrderStatus.NEW)).thenReturn(5L);

        long count = orderService.countByStatus(OrderStatus.NEW);
        assertEquals(5L, count);
    }

    @Test
    void getRecentOrders() {
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findRecentOrders(10)).thenReturn(orders);

        List<Order> result = orderService.getRecentOrders(10);
        assertEquals(1, result.size());
    }

    @Test
    void getOrderStatistics() {
        for (OrderStatus status : OrderStatus.values()) {
            when(orderRepository.countByStatus(status)).thenReturn(0L);
        }
        when(orderRepository.countByStatus(OrderStatus.NEW)).thenReturn(3L);
        when(orderRepository.countByStatus(OrderStatus.COOKING)).thenReturn(2L);

        Map<OrderStatus, Long> stats = orderService.getOrderStatistics();
        assertEquals(7, stats.size());
        assertEquals(3L, stats.get(OrderStatus.NEW));
        assertEquals(2L, stats.get(OrderStatus.COOKING));
    }

    @Test
    void getOrdersForCook() {
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findByStatusIn(anyList())).thenReturn(orders);

        List<Order> result = orderService.getOrdersForCook();
        assertEquals(1, result.size());
    }

    @Test
    void getOrdersForCourier() {
        Order deliveringOrder = new Order();
        deliveringOrder.setStatus(OrderStatus.DELIVERING);
        List<Order> orders = List.of(deliveringOrder);
        when(orderRepository.findByStatusIn(anyList())).thenReturn(orders);

        List<Order> result = orderService.getOrdersForCourier();
        assertEquals(1, result.size());
    }

    @Test
    void getOrdersByClientPhone() {
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findByClientPhone("+79991234567")).thenReturn(orders);

        List<Order> result = orderService.getOrdersByClientPhone("+79991234567");
        assertEquals(1, result.size());
    }

    @Test
    void createOrderCalculatesTotal() {
        OrderDto dto = new OrderDto();
        dto.setClientName("Иван");
        dto.setClientPhone("+79991234567");

        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Чай");
        dish2.setPrice(BigDecimal.valueOf(100));
        dish2.setAvailable(true);

        Map<Long, Integer> cart = new HashMap<>();
        cart.put(1L, 2);
        cart.put(2L, 3);

        when(dishRepository.findById(1L)).thenReturn(Optional.of(testDish));
        when(dishRepository.findById(2L)).thenReturn(Optional.of(dish2));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Long orderId = orderService.createOrder(dto, cart);
        assertNotNull(orderId);
    }
}
