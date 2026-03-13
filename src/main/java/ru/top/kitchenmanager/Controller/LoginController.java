package ru.top.kitchenmanager.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        // Получаем текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Проверяем, есть ли пользователь и аутентифицирован ли он
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getName().equals("anonymousUser")) {

            // Проверяем роли пользователя
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String role = authority.getAuthority();

                if (role.equals("ROLE_ADMIN")) {
                    return "redirect:/admin/dashboard";
                } else if (role.equals("ROLE_COOK")) {
                    return "redirect:/cook/orders";
                } else if (role.equals("ROLE_COURIER")) {
                    return "redirect:/courier/orders";
                }
            }
        }

        // Если не аутентифицирован или роль не определена - отправляем в меню клиента
        return "redirect:/client/menu";
    }
}