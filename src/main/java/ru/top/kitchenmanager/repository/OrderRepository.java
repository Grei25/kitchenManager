package ru.top.kitchenmanager.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.top.kitchenmanager.model.Order;
import ru.top.kitchenmanager.model.OrderStatus;
import ru.top.kitchenmanager.model.User;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByCourierAndStatus(User courier, OrderStatus status);
    List<Order> findByClientPhone(String phone);
}