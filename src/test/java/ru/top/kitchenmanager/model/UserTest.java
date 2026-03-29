package ru.top.kitchenmanager.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void userCreation() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("encoded_password");
        user.setFullName("Администратор");
        user.setPhone("+79991234567");
        user.setRole(UserRole.ADMIN);
        user.setActive(true);

        assertEquals(1L, user.getId());
        assertEquals("admin", user.getUsername());
        assertEquals("encoded_password", user.getPassword());
        assertEquals("Администратор", user.getFullName());
        assertEquals("+79991234567", user.getPhone());
        assertEquals(UserRole.ADMIN, user.getRole());
        assertTrue(user.getActive());
    }

    @Test
    void userDefaultActive() {
        User user = new User();
        assertTrue(user.getActive());
    }

    @Test
    void userRoles() {
        User admin = new User();
        admin.setRole(UserRole.ADMIN);
        assertEquals(UserRole.ADMIN, admin.getRole());

        User cook = new User();
        cook.setRole(UserRole.COOK);
        assertEquals(UserRole.COOK, cook.getRole());

        User courier = new User();
        courier.setRole(UserRole.COURIER);
        assertEquals(UserRole.COURIER, courier.getRole());

        User superAdmin = new User();
        superAdmin.setRole(UserRole.SUPER_ADMIN);
        assertEquals(UserRole.SUPER_ADMIN, superAdmin.getRole());
    }
}
