package ru.top.kitchenmanager.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DishDtoTest {

    @Test
    void dishDtoCreation() {
        DishDto dto = new DishDto();
        dto.setId(1L);
        dto.setName("Пицца");
        dto.setDescription("Тестовая пицца");
        dto.setPrice(BigDecimal.valueOf(500));
        dto.setCategory("Пицца");
        dto.setImageUrl("https://example.com/image.jpg");
        dto.setAvailable(true);

        assertEquals(1L, dto.getId());
        assertEquals("Пицца", dto.getName());
        assertEquals("Тестовая пицца", dto.getDescription());
        assertEquals(0, dto.getPrice().compareTo(BigDecimal.valueOf(500)));
        assertEquals("Пицца", dto.getCategory());
        assertEquals("https://example.com/image.jpg", dto.getImageUrl());
        assertTrue(dto.getAvailable());
    }

    @Test
    void dishDtoDefaultAvailable() {
        DishDto dto = new DishDto();
        assertTrue(dto.getAvailable());
    }
}
