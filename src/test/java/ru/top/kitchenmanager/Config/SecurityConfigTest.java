package ru.top.kitchenmanager.Config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.top.kitchenmanager.repository.DishRepository;
import ru.top.kitchenmanager.repository.OrderRepository;
import ru.top.kitchenmanager.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private DishRepository dishRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void loginPageAccessible() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void adminRequiresAuth() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void adminAccessibleWithRole() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "cook", authorities = "ROLE_COOK")
    void cookCannotAccessAdmin() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "cook", authorities = "ROLE_COOK")
    void cookCanAccessCookPage() throws Exception {
        mockMvc.perform(get("/cook/orders"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "courier", authorities = "ROLE_COURIER")
    void courierCanAccessCourierPage() throws Exception {
        mockMvc.perform(get("/courier/orders"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void adminCannotAccessCook() throws Exception {
        mockMvc.perform(get("/cook/orders"))
                .andExpect(status().isForbidden());
    }
}
