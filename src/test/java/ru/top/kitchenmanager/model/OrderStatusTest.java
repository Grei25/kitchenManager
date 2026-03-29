package ru.top.kitchenmanager.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @Test
    void allStatusesExist() {
        OrderStatus[] statuses = OrderStatus.values();
        assertEquals(7, statuses.length);
    }

    @Test
    void statusValues() {
        assertEquals("NEW", OrderStatus.NEW.name());
        assertEquals("CONFIRMED", OrderStatus.CONFIRMED.name());
        assertEquals("COOKING", OrderStatus.COOKING.name());
        assertEquals("READY", OrderStatus.READY.name());
        assertEquals("DELIVERING", OrderStatus.DELIVERING.name());
        assertEquals("DELIVERED", OrderStatus.DELIVERED.name());
        assertEquals("CANCELLED", OrderStatus.CANCELLED.name());
    }

    @Test
    void valueOf() {
        assertEquals(OrderStatus.NEW, OrderStatus.valueOf("NEW"));
        assertEquals(OrderStatus.CONFIRMED, OrderStatus.valueOf("CONFIRMED"));
    }
}
