package ru.top.kitchenmanager.Controller.client;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.top.kitchenmanager.dto.OrderDto;
import ru.top.kitchenmanager.model.Dish;
import ru.top.kitchenmanager.service.DishService;
import ru.top.kitchenmanager.service.OrderService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private DishService dishService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/menu")
    public String menu(Model model, HttpSession session) {
        List<Dish> dishes = dishService.getAllAvailableDishes();
        model.addAttribute("dishes", dishes);

        // Получаем корзину из сессии
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        model.addAttribute("cart", cart);

        return "client/menu";
    }

    @PostMapping("/cart/add/{dishId}")
    public String addToCart(@PathVariable Long dishId, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        cart.put(dishId, cart.getOrDefault(dishId, 0) + 1);
        session.setAttribute("cart", cart);
        return "redirect:/client/menu";
    }

    @PostMapping("/cart/remove/{dishId}")
    public String removeFromCart(@PathVariable Long dishId, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart != null) {
            cart.remove(dishId);
            session.setAttribute("cart", cart);
        }
        return "redirect:/client/menu";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/client/menu";
        }

        List<Dish> cartDishes = new ArrayList<>();
        double total = 0;
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Dish dish = dishService.getDishById(entry.getKey());
            if (dish != null) {
                cartDishes.add(dish);
                total += dish.getPrice().doubleValue() * entry.getValue();
            }
        }

        model.addAttribute("cartDishes", cartDishes);
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);

        return "client/checkout";
    }

    @PostMapping("/order/create")
    public String createOrder(@ModelAttribute OrderDto orderDto, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/client/menu";
        }

        Long orderId = orderService.createOrder(orderDto, cart);
        session.removeAttribute("cart"); // Очищаем корзину

        return "redirect:/client/order/" + orderId + "/track";
    }

    @GetMapping("/order/{id}/track")
    public String trackOrder(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "client/track";
    }
}