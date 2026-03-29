package ru.top.kitchenmanager.Controller.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.top.kitchenmanager.Config.SecurityConfig;
import ru.top.kitchenmanager.model.Dish;
import ru.top.kitchenmanager.service.DishService;
import ru.top.kitchenmanager.service.OrderService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
@Import(SecurityConfig.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DishService dishService;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(username = "client", authorities = "ROLE_CLIENT")
    void menuAccessible() throws Exception {
        when(dishService.getAvailableDishesByCategory(any(), any()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/client/menu"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/menu"));
    }

    @Test
    @WithMockUser(username = "client", authorities = "ROLE_CLIENT")
    void checkoutAccessible() throws Exception {
        // Корзина пуста - будет редирект на menu
        mockMvc.perform(get("/client/checkout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/menu"));
    }

    @Test
    @WithMockUser(username = "client", authorities = "ROLE_CLIENT")
    void checkoutWithItemsAccessible() throws Exception {
        // Создаём моки для блюд
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Пицца");
        dish.setPrice(java.math.BigDecimal.valueOf(500));

        when(dishService.getDishById(1L)).thenReturn(dish);

        mockMvc.perform(get("/client/checkout")
                        .sessionAttr("cart", java.util.Map.of(1L, 2)))
                .andExpect(status().isOk())
                .andExpect(view().name("client/checkout"));
    }
}
