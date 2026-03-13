package ru.top.kitchenmanager.service;

import ru.top.kitchenmanager.model.Dish;

import java.util.List;

public interface DishService {
    List<Dish> getAllAvailableDishes();
    Dish getDishById(Long id);
    Dish saveDish(Dish dish);
    void deleteDish(Long id);
}