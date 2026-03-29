package ru.top.kitchenmanager.Controller.cook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.top.kitchenmanager.model.Order;
import ru.top.kitchenmanager.model.OrderStatus;
import ru.top.kitchenmanager.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/cook")
public class CookController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String cookingOrders(Model model) {
        List<Order> confirmedOrders = orderService.getOrdersByStatus(OrderStatus.CONFIRMED);
        List<Order> cookingOrders = orderService.getOrdersByStatus(OrderStatus.COOKING);

        model.addAttribute("confirmedOrders", confirmedOrders);
        model.addAttribute("cookingOrders", cookingOrders);
        model.addAttribute("orders", orderService.getOrdersForCook());
        return "cook/orders";
    }

    @PostMapping("/order/{id}/start-cooking")
    public String startCooking(@PathVariable Long id) {
        orderService.updateOrderStatus(id, OrderStatus.COOKING);
        return "redirect:/cook/orders";
    }

    @PostMapping("/order/{id}/ready")
    public String markAsReady(@PathVariable Long id) {
        orderService.updateOrderStatus(id, OrderStatus.READY);
        return "redirect:/cook/orders";
    }
}