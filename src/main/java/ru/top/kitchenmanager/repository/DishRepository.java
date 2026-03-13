package ru.top.kitchenmanager.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.top.kitchenmanager.model.Dish;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByAvailableTrue();
    List<Dish> findByCategory(String category);
}