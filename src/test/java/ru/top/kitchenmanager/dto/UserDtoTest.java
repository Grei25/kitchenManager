package ru.top.kitchenmanager.dto;

import org.junit.jupiter.api.Test;
import ru.top.kitchenmanager.model.UserRole;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    @Test
    void userDtoCreation() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setUsername("admin");
        dto.setPassword("password123");
        dto.setFullName("Администратор");
        dto.setPhone("+79991234567");
        dto.setRole(UserRole.ADMIN);

        assertEquals(1L, dto.getId());
        assertEquals("admin", dto.getUsername());
        assertEquals("password123", dto.getPassword());
        assertEquals("Администратор", dto.getFullName());
        assertEquals("+79991234567", dto.getPhone());
        assertEquals(UserRole.ADMIN, dto.getRole());
    }

    @Test
    void userDtoConstructor() {
        UserDto dto = new UserDto(1L, "admin", "Администратор", "+79991234567", UserRole.ADMIN);

        assertEquals(1L, dto.getId());
        assertEquals("admin", dto.getUsername());
        assertEquals("Администратор", dto.getFullName());
        assertEquals("+79991234567", dto.getPhone());
        assertEquals(UserRole.ADMIN, dto.getRole());
        assertNull(dto.getPassword());
    }
}
