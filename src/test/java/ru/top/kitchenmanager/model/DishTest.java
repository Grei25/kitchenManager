package ru.top.kitchenmanager.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DishTest {

    @Test
    void dishCreation() {
        Dish dish = new Dish();
        dish.setName("Пицца");
        dish.setDescription("Тестовая пицца");
        dish.setPrice(BigDecimal.valueOf(500));
        dish.setCategory("Пицца");
        dish.setAvailable(true);

        assertEquals("Пицца", dish.getName());
        assertEquals("Тестовая пицца", dish.getDescription());
        assertEquals(0, dish.getPrice().compareTo(BigDecimal.valueOf(500)));
        assertEquals("Пицца", dish.getCategory());
        assertTrue(dish.getAvailable());
    }

    @Test
    void dishDefaultAvailable() {
        Dish dish = new Dish();
        assertTrue(dish.getAvailable());
    }

    @Test
    void dishWithImageUrl() {
        Dish dish = new Dish();
        dish.setImageUrl("https://example.com/image.jpg");
        assertEquals("https://example.com/image.jpg", dish.getImageUrl());
    }

    @Test
    void dishId() {
        Dish dish = new Dish();
        dish.setId(1L);
        assertEquals(1L, dish.getId());
    }
}
