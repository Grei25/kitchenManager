package ru.top.kitchenmanager.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderDtoTest {

    @Test
    void orderDtoCreation() {
        OrderDto dto = new OrderDto();
        dto.setClientName("Иван");
        dto.setClientPhone("+79991234567");
        dto.setAddress("ул. Пушкина, д. 10");
        dto.setComment("Позвонить заранее");
        dto.setPickup(false);

        assertEquals("Иван", dto.getClientName());
        assertEquals("+79991234567", dto.getClientPhone());
        assertEquals("ул. Пушкина, д. 10", dto.getAddress());
        assertEquals("Позвонить заранее", dto.getComment());
        assertFalse(dto.isPickup());
    }

    @Test
    void orderDtoPickup() {
        OrderDto dto = new OrderDto();
        dto.setPickup(true);
        assertTrue(dto.isPickup());
    }

    @Test
    void orderDtoPickupDefault() {
        OrderDto dto = new OrderDto();
        assertFalse(dto.isPickup());
    }
}
