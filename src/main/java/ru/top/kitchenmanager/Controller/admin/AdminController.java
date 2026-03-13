package ru.top.kitchenmanager.Controller.admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.top.kitchenmanager.model.Order;
import ru.top.kitchenmanager.model.OrderStatus;
import ru.top.kitchenmanager.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Order> newOrders = orderService.getOrdersByStatus(OrderStatus.NEW);
        List<Order> confirmedOrders = orderService.getOrdersByStatus(OrderStatus.CONFIRMED);

        model.addAttribute("newOrders", newOrders);
        model.addAttribute("confirmedOrders", confirmedOrders);
        return "admin/dashboard";
    }

    @PostMapping("/order/{id}/confirm")
    public String confirmOrder(@PathVariable Long id) {
        orderService.updateOrderStatus(id, OrderStatus.CONFIRMED);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/orders/all")
    public String allOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }
}