package ru.top.kitchenmanager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.kitchenmanager.dto.DishDto;
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
    public Page<Dish> getAvailableDishesPaginated(Pageable pageable) {
        return dishRepository.findByAvailableTrue(pageable);
    }
    
    @Override
    public Page<Dish> getAvailableDishesByCategory(String category, Pageable pageable) {
        if (category == null || category.isEmpty() || category.equals("all")) {
            return dishRepository.findByAvailableTrue(pageable);
        }
        return dishRepository.findByAvailableTrueAndCategory(category, pageable);
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
    @Transactional
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
    
    @Override
    public Page<Dish> getAllDishesPaginated(Pageable pageable) {
        return dishRepository.findAll(pageable);
    }
    
    @Override
    @Transactional
    public Dish createDish(DishDto dishDto) {
        Dish dish = new Dish();
        dish.setName(dishDto.getName());
        dish.setDescription(dishDto.getDescription());
        dish.setPrice(dishDto.getPrice());
        dish.setCategory(dishDto.getCategory());
        dish.setImageUrl(dishDto.getImageUrl());
        dish.setAvailable(dishDto.getAvailable() != null ? dishDto.getAvailable() : true);
        return dishRepository.save(dish);
    }
    
    @Override
    @Transactional
    public Dish updateDish(Long id, DishDto dishDto) {
        Dish dish = getDishById(id);
        if (dish == null) {
            return null;
        }
        dish.setName(dishDto.getName());
        dish.setDescription(dishDto.getDescription());
        dish.setPrice(dishDto.getPrice());
        dish.setCategory(dishDto.getCategory());
        dish.setImageUrl(dishDto.getImageUrl());
        dish.setAvailable(dishDto.getAvailable());
        return dishRepository.save(dish);
    }
}
