package ru.top.kitchenmanager.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.top.kitchenmanager.model.Dish;
import ru.top.kitchenmanager.repository.DishRepository;
import ru.top.kitchenmanager.service.DishService;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishRepository dishRepository;

    @Override
    public List<Dish> getAllAvailableDishes() {
        return dishRepository.findByAvailableTrue();
    }

    @Override
    public Dish getDishById(Long id) {
        return dishRepository.findById(id).orElse(null);
    }

    @Override
    public Dish saveDish(Dish dish) {
        return dishRepository.save(dish);
    }

    @Override
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}