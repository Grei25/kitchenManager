package ru.top.kitchenmanager.Controller.admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.kitchenmanager.model.Order;
import ru.top.kitchenmanager.model.OrderStatus;
import ru.top.kitchenmanager.model.UserRole;
import ru.top.kitchenmanager.service.DishService;
import ru.top.kitchenmanager.service.OrderService;
import ru.top.kitchenmanager.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private DishService dishService;

@GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Списки заказов
        model.addAttribute("newOrdersList", orderService.getOrdersByStatus(OrderStatus.NEW));
        model.addAttribute("confirmedOrders", orderService.getOrdersByStatus(OrderStatus.CONFIRMED));

        // Заказы для самовывоза
        model.addAttribute("acceptedOrders", orderService.getOrdersByStatus(OrderStatus.ACCEPTED));
        model.addAttribute("cookingOrders", orderService.getOrdersByStatus(OrderStatus.COOKING));
        model.addAttribute("readyOrders", orderService.getOrdersByStatus(OrderStatus.READY));

        // Статистика (числа)
        model.addAttribute("newOrdersCount", orderService.countByStatus(OrderStatus.NEW));
        model.addAttribute("cookingOrdersCount", orderService.countByStatus(OrderStatus.COOKING));
        model.addAttribute("deliveringOrdersCount", orderService.countByStatus(OrderStatus.DELIVERING));

        // Сотрудники
        model.addAttribute("activeCooks", userService.countActiveByRole(UserRole.COOK));
        model.addAttribute("activeCouriers", userService.countActiveByRole(UserRole.COURIER));

        // Последние заказы
        model.addAttribute("recentOrders", orderService.getRecentOrders(10));
        model.addAttribute("orders", orderService.getOrdersForAdmin());

        return "admin/dashboard";
    }

    @PostMapping("/order/{id}/confirm")
    public String confirmOrder(@PathVariable Long id) {
        orderService.updateOrderStatus(id, OrderStatus.CONFIRMED);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/order/{id}/accept")
    public String acceptOrder(@PathVariable Long id) {
        orderService.acceptOrder(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/order/{id}/complete")
    public String completeOrder(@PathVariable Long id) {
        orderService.completeOrder(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/orders/all")
    public String allOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    @GetMapping("/order/create")
    public String createOrderForm(Model model) {
        model.addAttribute("dishes", dishService.getAllAvailableDishes());
        return "admin/create-order";
    }

    @PostMapping("/order/create")
    public String createOrder(@RequestParam String clientName,
                              @RequestParam String clientPhone,
                              @RequestParam(required = false) String address,
                              @RequestParam(required = false) String comment,
                              @RequestParam(required = false, defaultValue = "false") boolean pickup,
                              @RequestParam String cartData,
                              RedirectAttributes redirectAttributes) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Integer> cart = mapper.readValue(cartData, Map.class);

            Map<Long, Integer> cartLong = new HashMap<>();
            for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                cartLong.put(Long.parseLong(entry.getKey()), entry.getValue());
            }

            ru.top.kitchenmanager.dto.OrderDto orderDto = new ru.top.kitchenmanager.dto.OrderDto();
            orderDto.setClientName(clientName);
            orderDto.setClientPhone(clientPhone);
            orderDto.setAddress(address);
            orderDto.setComment(comment);
            orderDto.setPickup(pickup);

            Long orderId = orderService.createOrder(orderDto, cartLong);
            redirectAttributes.addFlashAttribute("successMessage", "Заказ #" + orderId + " создан!");
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
            return "redirect:/admin/order/create";
        }
    }

}

