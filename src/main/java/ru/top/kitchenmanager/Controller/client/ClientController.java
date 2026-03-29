package ru.top.kitchenmanager.Controller.client;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private static final int PAGE_SIZE = 6;

    @Autowired
    private DishService dishService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/menu")
    public String menu(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String category,
            Model model, 
            HttpSession session) {
        
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Dish> dishPage = dishService.getAvailableDishesByCategory(category, pageable);
        
        model.addAttribute("dishes", dishPage.getContent());
        model.addAttribute("dishPage", dishPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", dishPage.getTotalPages());
        model.addAttribute("selectedCategory", category != null ? category : "all");

        Map<Long, Integer> cart = getCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("cartCount", calculateCartCount(cart));

        return "client/menu";
    }

    @PostMapping("/cart/add/{dishId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(
            @PathVariable Long dishId,
            HttpSession session) {
        
        Map<Long, Integer> cart = getCart(session);
        cart.put(dishId, cart.getOrDefault(dishId, 0) + 1);
        session.setAttribute("cart", cart);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("cartCount", calculateCartCount(cart));
        response.put("message", "Блюдо добавлено в корзину");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cart/count")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCartCount(HttpSession session) {
        Map<Long, Integer> cart = getCart(session);
        int count = calculateCartCount(cart);
        
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        Map<Long, Integer> cart = getCart(session);

        if (cart.isEmpty()) {
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
        Map<Long, Integer> cart = getCart(session);
        if (cart.isEmpty()) {
            return "redirect:/client/menu";
        }

        Long orderId = orderService.createOrder(orderDto, cart);
        session.removeAttribute("cart");

        return "redirect:/client/order/" + orderId + "/track";
    }

    @GetMapping("/order/{id}/track")
    public String trackOrder(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "client/track";
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/client/menu";
    }

    private Map<Long, Integer> getCart(HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private int calculateCartCount(Map<Long, Integer> cart) {
        return cart.values().stream().mapToInt(Integer::intValue).sum();
    }
}
