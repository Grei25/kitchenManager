package ru.top.kitchenmanager.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.top.kitchenmanager.model.User;
import ru.top.kitchenmanager.model.UserRole;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(UserRole role);
    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR u.phone LIKE CONCAT('%', :query, '%')")
    List<User> searchByFullNameOrPhone(@Param("query") String query);

    // Проверка существования username, исключая конкретный id
    boolean existsByUsernameAndIdNot(String username, Long id);

    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    List<User> findRecentUsers(Pageable pageable);

    @Modifying
    void deleteAllByIdIn(List<Long> ids);
}