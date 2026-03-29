package ru.top.kitchenmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.top.kitchenmanager.repository.DishRepository;
import ru.top.kitchenmanager.repository.OrderRepository;
import ru.top.kitchenmanager.repository.UserRepository;

@SpringBootTest
class KitchenManagerApplicationTests {

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private DishRepository dishRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

}
