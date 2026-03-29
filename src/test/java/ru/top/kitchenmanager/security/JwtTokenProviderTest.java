package ru.top.kitchenmanager.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        org.springframework.test.util.ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret",
                "mySuperSecretKeyForJwtTokenGenerationThatIsLongEnoughForHS512Algorithm1234567890abcdef");
        org.springframework.test.util.ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpiration", 86400000L);
    }

    @Test
    void generateToken() {
        String token = jwtTokenProvider.generateToken("admin", List.of("ROLE_ADMIN"));
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getUsernameFromToken() {
        String token = jwtTokenProvider.generateToken("admin", List.of("ROLE_ADMIN"));
        String username = jwtTokenProvider.getUsernameFromToken(token);
        assertEquals("admin", username);
    }

    @Test
    void getRolesFromToken() {
        String token = jwtTokenProvider.generateToken("admin", List.of("ROLE_ADMIN"));
        List<String> roles = jwtTokenProvider.getRolesFromToken(token);
        assertEquals(1, roles.size());
        assertEquals("ROLE_ADMIN", roles.get(0));
    }

    @Test
    void validateToken() {
        String token = jwtTokenProvider.generateToken("admin", List.of("ROLE_ADMIN"));
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateInvalidToken() {
        assertFalse(jwtTokenProvider.validateToken("invalid.token.here"));
    }

    @Test
    void validateEmptyToken() {
        assertFalse(jwtTokenProvider.validateToken(""));
    }

    @Test
    void multipleRoles() {
        String token = jwtTokenProvider.generateToken("superadmin", List.of("ROLE_ADMIN", "ROLE_SUPER_ADMIN"));
        List<String> roles = jwtTokenProvider.getRolesFromToken(token);
        assertEquals(2, roles.size());
    }
}
