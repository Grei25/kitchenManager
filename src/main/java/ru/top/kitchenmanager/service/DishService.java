package ru.top.kitchenmanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.top.kitchenmanager.dto.DishDto;
import ru.top.kitchenmanager.model.Dish;

import java.util.List;

public interface DishService {
    List<Dish> getAllAvailableDishes();
    
    Page<Dish> getAvailableDishesPaginated(Pageable pageable);
    
    Page<Dish> getAvailableDishesByCategory(String category, Pageable pageable);
    
    Dish getDishById(Long id);
    Dish saveDish(Dish dish);
    void deleteDish(Long id);
    
    Page<Dish> getAllDishesPaginated(Pageable pageable);
    
    Dish createDish(DishDto dishDto);
    Dish updateDish(Long id, DishDto dishDto);
}
