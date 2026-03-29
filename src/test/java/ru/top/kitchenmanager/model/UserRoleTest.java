package ru.top.kitchenmanager.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {

    @Test
    void allRolesExist() {
        UserRole[] roles = UserRole.values();
        assertEquals(5, roles.length);
    }

    @Test
    void roleValues() {
        assertEquals("CLIENT", UserRole.CLIENT.name());
        assertEquals("ADMIN", UserRole.ADMIN.name());
        assertEquals("COOK", UserRole.COOK.name());
        assertEquals("COURIER", UserRole.COURIER.name());
        assertEquals("SUPER_ADMIN", UserRole.SUPER_ADMIN.name());
    }

    @Test
    void valueOf() {
        assertEquals(UserRole.ADMIN, UserRole.valueOf("ADMIN"));
        assertEquals(UserRole.SUPER_ADMIN, UserRole.valueOf("SUPER_ADMIN"));
    }
}
