package ru.top.kitchenmanager.Controller.courier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.top.kitchenmanager.model.Order;
import ru.top.kitchenmanager.model.OrderStatus;
import ru.top.kitchenmanager.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/courier")
public class CourierController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String availableOrders(Model model) {
        List<Order> readyOrders = orderService.getOrdersByStatus(OrderStatus.READY);
        model.addAttribute("readyOrders", readyOrders);
        model.addAttribute("orders", orderService.getOrdersForCourier());
        return "courier/orders";
    }

    @GetMapping("/my-deliveries")
    public String myDeliveries(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Пока просто заглушка, позже привяжем к реальному курьеру
        model.addAttribute("deliveringOrders",
                orderService.getOrdersByStatus(OrderStatus.DELIVERING));
        return "courier/deliveries";
    }

    @PostMapping("/order/{id}/take")
    public String takeOrder(@PathVariable Long id) {
        orderService.updateOrderStatus(id, OrderStatus.DELIVERING);
        return "redirect:/courier/orders";
    }

    @PostMapping("/order/{id}/delivered")
    public String markAsDelivered(@PathVariable Long id) {
        orderService.updateOrderStatus(id, OrderStatus.DELIVERED);
        return "redirect:/courier/my-deliveries";
    }
}