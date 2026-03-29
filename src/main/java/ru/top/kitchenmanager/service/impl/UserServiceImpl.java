package ru.top.kitchenmanager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.kitchenmanager.dto.UserDto;
import ru.top.kitchenmanager.model.User;

import ru.top.kitchenmanager.model.UserRole;
import ru.top.kitchenmanager.repository.UserRepository;
import ru.top.kitchenmanager.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + id));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));
    }

    @Override
    @Transactional
    public User createUser(UserDto userDto) {
        // Проверяем, нет ли уже такого username
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFullName(userDto.getFullName());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long id, UserDto userDto) {
        User user = getUserById(id);

        user.setFullName(userDto.getFullName());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());

        // Если указан новый пароль — обновляем
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public long countActiveByRole(UserRole role) {
        return userRepository.findByRole(role).size();
    }

    // ============ НОВЫЕ МЕТОДЫ ============

    /**
     * Получить статистику по пользователям (сколько админов, поваров, курьеров)
     */
    @Override
    public Map<UserRole, Long> getUserStatistics() {
        Map<UserRole, Long> stats = new HashMap<>();
        for (UserRole role : UserRole.values()) {
            stats.put(role, (long) userRepository.findByRole(role).size());
        }
        return stats;
    }

    /**
     * Поиск пользователей по имени или телефону
     */
    @Override
    public List<User> searchUsers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllUsers();
        }
        return userRepository.searchByFullNameOrPhone(query);
    }

    /**
     * Проверить, существует ли пользователь с таким логином (кроме указанного id)
     */
    @Override
    public boolean existsByUsernameExceptId(String username, Long excludeId) {
        return userRepository.existsByUsernameAndIdNot(username, excludeId);
    }
    @Override
    public List<User> getRecentUsers(int limit) {
        return userRepository.findRecentUsers(PageRequest.of(0, limit));
    }


    /**
     * Обновить статус активности (если добавим поле isActive)
     */
    @Override
    @Transactional
    public User updateUserStatus(Long id, boolean isActive) {
        User user = getUserById(id);
        user.setActive(isActive);  // нужно добавить поле в User
        return userRepository.save(user);
    }

    /**
     * Массовое удаление пользователей по списку id
     */
    @Override
    @Transactional
    public void deleteUsers(List<Long> ids) {
        userRepository.deleteAllByIdIn(ids);
    }

    /**
     * Получить всех пользователей с пагинацией (для больших списков)
     */
    @Override
    public List<User> getUsersPaginated(int page, int size) {
        // Простейшая реализация, лучше использовать Pageable
        List<User> all = getAllUsers();
        int start = page * size;
        int end = Math.min(start + size, all.size());
        if (start >= all.size()) return List.of();
        return all.subList(start, end);
    }
}