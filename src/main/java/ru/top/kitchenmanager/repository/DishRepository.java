package ru.top.kitchenmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.top.kitchenmanager.model.Dish;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByAvailableTrue();
    
    Page<Dish> findByAvailableTrue(Pageable pageable);
    
    List<Dish> findByAvailableTrueAndCategory(String category);
    
    Page<Dish> findByAvailableTrueAndCategory(String category, Pageable pageable);
    
    List<Dish> findByCategory(String category);
    
    Page<Dish> findByCategory(String category, Pageable pageable);
}
