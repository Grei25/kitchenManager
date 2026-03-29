package ru.top.kitchenmanager.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void orderCreation() {
        Order order = new Order();
        order.setClientName("Иван");
        order.setClientPhone("+79991234567");
        order.setAddress("ул. Пушкина, д. 10");
        order.setStatus(OrderStatus.NEW);
        order.setTotalAmount(BigDecimal.valueOf(1000));

        assertEquals("Иван", order.getClientName());
        assertEquals("+79991234567", order.getClientPhone());
        assertEquals("ул. Пушкина, д. 10", order.getAddress());
        assertEquals(OrderStatus.NEW, order.getStatus());
        assertEquals(0, order.getTotalAmount().compareTo(BigDecimal.valueOf(1000)));
    }

    @Test
    void orderDefaultStatus() {
        Order order = new Order();
        assertEquals(OrderStatus.NEW, order.getStatus());
    }

    @Test
    void orderPickup() {
        Order order = new Order();
        order.setPickup(true);
        assertTrue(order.getPickup());

        order.setPickup(false);
        assertFalse(order.getPickup());
    }

    @Test
    void orderPickupDefaultFalse() {
        Order order = new Order();
        assertFalse(order.getPickup());
    }

    @Test
    void orderPickupNull() {
        Order order = new Order();
        order.setPickup(null);
        assertNull(order.getPickup());
    }

    @Test
    void orderTimestamps() {
        Order order = new Order();
        order.onCreate();
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
    }

    @Test
    void orderUpdateTimestamp() {
        Order order = new Order();
        order.onCreate();
        LocalDateTime created = order.getCreatedAt();

        try { Thread.sleep(10); } catch (InterruptedException e) {}

        order.onUpdate();
        assertNotNull(order.getUpdatedAt());
        assertEquals(created, order.getCreatedAt());
    }

    @Test
    void orderItems() {
        Order order = new Order();
        assertNotNull(order.getItems());
        assertTrue(order.getItems().isEmpty());
    }
}
