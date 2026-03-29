package ru.top.kitchenmanager.Controller.courier;

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

@WebMvcTest(CourierController.class)
@Import(SecurityConfig.class)
class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(username = "courier", authorities = "ROLE_COURIER")
    void courierOrdersAccessible() throws Exception {
        when(orderService.getOrdersByStatus(any())).thenReturn(Collections.emptyList());
        when(orderService.getOrdersForCourier()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/courier/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("courier/orders"));
    }

    @Test
    void courierOrdersRequiresAuth() throws Exception {
        mockMvc.perform(get("/courier/orders"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void courierOrdersForbiddenForAdmin() throws Exception {
        mockMvc.perform(get("/courier/orders"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "courier", authorities = "ROLE_COURIER")
    void myDeliveriesAccessible() throws Exception {
        when(orderService.getOrdersByStatus(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/courier/my-deliveries"))
                .andExpect(status().isOk())
                .andExpect(view().name("courier/deliveries"));
    }

    @Test
    @WithMockUser(username = "courier", authorities = "ROLE_COURIER")
    void takeOrder() throws Exception {
        mockMvc.perform(post("/courier/order/1/take"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courier/orders"));
    }

    @Test
    @WithMockUser(username = "courier", authorities = "ROLE_COURIER")
    void markAsDelivered() throws Exception {
        mockMvc.perform(post("/courier/order/1/delivered"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courier/my-deliveries"));
    }
}
