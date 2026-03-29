package ru.top.kitchenmanager.Controller.cook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.top.kitchenmanager.Config.SecurityConfig;
import ru.top.kitchenmanager.service.OrderService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CookController.class)
@Import(SecurityConfig.class)
class CookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(username = "cook", authorities = "ROLE_COOK")
    void cookOrdersAccessible() throws Exception {
        when(orderService.getOrdersByStatus(any())).thenReturn(Collections.emptyList());
        when(orderService.getOrdersForCook()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cook/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("cook/orders"));
    }

    @Test
    void cookOrdersRequiresAuth() throws Exception {
        mockMvc.perform(get("/cook/orders"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void cookOrdersForbiddenForAdmin() throws Exception {
        mockMvc.perform(get("/cook/orders"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "cook", authorities = "ROLE_COOK")
    void startCooking() throws Exception {
        mockMvc.perform(post("/cook/order/1/start-cooking"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cook/orders"));
    }

    @Test
    @WithMockUser(username = "cook", authorities = "ROLE_COOK")
    void markAsReady() throws Exception {
        mockMvc.perform(post("/cook/order/1/ready"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cook/orders"));
    }
}
