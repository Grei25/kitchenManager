package ru.top.kitchenmanager.Controller.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.top.kitchenmanager.Config.SecurityConfig;
import ru.top.kitchenmanager.service.DishService;
import ru.top.kitchenmanager.service.OrderService;
import ru.top.kitchenmanager.service.UserService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    @MockBean
    private DishService dishService;

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void dashboardAccessible() throws Exception {
        when(orderService.getOrdersByStatus(any())).thenReturn(Collections.emptyList());
        when(orderService.countByStatus(any())).thenReturn(0L);
        when(userService.countActiveByRole(any())).thenReturn(0L);
        when(orderService.getRecentOrders(10)).thenReturn(Collections.emptyList());
        when(orderService.getOrdersForAdmin()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"));
    }

    @Test
    void dashboardRequiresAuth() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "cook", authorities = "ROLE_COOK")
    void dashboardForbiddenForCook() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void allOrdersAccessible() throws Exception {
        when(orderService.getAllOrders()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/orders/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/orders"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void createOrderFormAccessible() throws Exception {
        when(dishService.getAllAvailableDishes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/create-order"));
    }
}
