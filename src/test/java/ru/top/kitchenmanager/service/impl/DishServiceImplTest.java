package ru.top.kitchenmanager.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.top.kitchenmanager.model.Dish;
import ru.top.kitchenmanager.repository.DishRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceImplTest {

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private DishServiceImpl dishService;

    private Dish testDish;

    @BeforeEach
    void setUp() {
        testDish = new Dish();
        testDish.setId(1L);
        testDish.setName("Пицца");
        testDish.setPrice(BigDecimal.valueOf(500));
        testDish.setCategory("Пицца");
        testDish.setAvailable(true);
    }

    @Test
    void getAllAvailableDishes() {
        when(dishRepository.findByAvailableTrue()).thenReturn(List.of(testDish));
        List<Dish> result = dishService.getAllAvailableDishes();
        assertEquals(1, result.size());
    }

    @Test
    void getDishById() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(testDish));
        Dish result = dishService.getDishById(1L);
        assertNotNull(result);
        assertEquals("Пицца", result.getName());
    }

    @Test
    void getDishByIdNotFound() {
        when(dishRepository.findById(999L)).thenReturn(Optional.empty());
        Dish result = dishService.getDishById(999L);
        assertNull(result);
    }

    @Test
    void saveDish() {
        when(dishRepository.save(any(Dish.class))).thenReturn(testDish);
        Dish result = dishService.saveDish(testDish);
        assertNotNull(result);
        verify(dishRepository).save(testDish);
    }

    @Test
    void deleteDish() {
        dishService.deleteDish(1L);
        verify(dishRepository).deleteById(1L);
    }
}
