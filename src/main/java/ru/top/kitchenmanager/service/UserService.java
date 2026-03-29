package ru.top.kitchenmanager.service;
import ru.top.kitchenmanager.dto.UserDto;
import ru.top.kitchenmanager.model.User;
import ru.top.kitchenmanager.model.UserRole;


import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> getAllUsers();
    List<User> getUsersByRole(UserRole role);
    User getUserById(Long id);
    User getUserByUsername(String username);
    User createUser(UserDto userDto);
    User updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    long countActiveByRole(UserRole role);
    Map<UserRole, Long> getUserStatistics();
    List<User> searchUsers(String query);
    boolean existsByUsernameExceptId(String username, Long excludeId);
    List<User> getRecentUsers(int limit);  // ← этот метод должен быть
    User updateUserStatus(Long id, boolean isActive);
    void deleteUsers(List<Long> ids);
    List<User> getUsersPaginated(int page, int size);
}