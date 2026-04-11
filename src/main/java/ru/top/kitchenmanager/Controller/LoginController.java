package ru.top.kitchenmanager.Controller;

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() &&
                !auth.getName().equals("anonymousUser")) {

            for (GrantedAuthority authority : auth.getAuthorities()) {
                String role = authority.getAuthority();

                if (role.equals("ROLE_SUPER_ADMIN") || role.equals("ROLE_ADMIN")) {
                    return "redirect:/admin/dashboard";
                } else if (role.equals("ROLE_COOK")) {
                    return "redirect:/cook/orders";
                } else if (role.equals("ROLE_COURIER")) {
                    return "redirect:/courier/orders";
                }
            }
        }

        return "redirect:/client/menu";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
