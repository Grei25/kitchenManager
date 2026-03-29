package ru.top.kitchenmanager.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void orderItemCreation() {
        OrderItem item = new OrderItem();
        Order order = new Order();
        Dish dish = new Dish();

        item.setId(1L);
        item.setOrder(order);
        item.setDish(dish);
        item.setQuantity(2);
        item.setPrice(BigDecimal.valueOf(500));

        assertEquals(1L, item.getId());
        assertEquals(order, item.getOrder());
        assertEquals(dish, item.getDish());
        assertEquals(2, item.getQuantity());
        assertEquals(0, item.getPrice().compareTo(BigDecimal.valueOf(500)));
    }

    @Test
    void orderItemDefault() {
        OrderItem item = new OrderItem();
        assertNull(item.getId());
        assertNull(item.getOrder());
        assertNull(item.getDish());
        assertNull(item.getQuantity());
        assertNull(item.getPrice());
    }
}
