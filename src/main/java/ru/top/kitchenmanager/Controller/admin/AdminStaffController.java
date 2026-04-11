package ru.top.kitchenmanager.Controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.kitchenmanager.dto.UserDto;
import ru.top.kitchenmanager.model.User;
import ru.top.kitchenmanager.model.UserRole;
import ru.top.kitchenmanager.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/staff")
public class AdminStaffController {

    @Autowired
    private UserService userService;

    private boolean isSuperAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));
    }

    private UserRole getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        for (GrantedAuthority authority : auth.getAuthorities()) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_SUPER_ADMIN")) return UserRole.SUPER_ADMIN;
            if (role.equals("ROLE_ADMIN")) return UserRole.ADMIN;
            if (role.equals("ROLE_COOK")) return UserRole.COOK;
            if (role.equals("ROLE_COURIER")) return UserRole.COURIER;
        }
        return null;
    }

    private List<UserRole> getAvailableRolesForCreation() {
        List<UserRole> roles = new ArrayList<>();
        roles.add(UserRole.COOK);
        roles.add(UserRole.COURIER);
        // Только SUPER_ADMIN может создавать ADMIN
        if (isSuperAdmin()) {
            roles.add(UserRole.ADMIN);
        }
        return roles;
    }

    // ============ СПИСОК СОТРУДНИКОВ ============

    @GetMapping
    public String listStaff(Model model) {
        List<User> cooks = userService.getUsersByRole(UserRole.COOK);
        List<User> couriers = userService.getUsersByRole(UserRole.COURIER);
        List<User> admins = isSuperAdmin() ? userService.getUsersByRole(UserRole.ADMIN) : List.of();

        model.addAttribute("cooks", cooks);
        model.addAttribute("couriers", couriers);
        model.addAttribute("admins", admins);
        model.addAttribute("isSuperAdmin", isSuperAdmin());
        model.addAttribute("stats", userService.getUserStatistics());

        return "admin/staff/list";
    }

    // ============ ПОИСК ============

    @GetMapping("/search")
    public String searchStaff(@RequestParam(required = false) String query, Model model) {
        List<User> searchResults = userService.searchUsers(query);

        List<User> cooks = searchResults.stream()
                .filter(u -> u.getRole() == UserRole.COOK)
                .toList();
        List<User> couriers = searchResults.stream()
                .filter(u -> u.getRole() == UserRole.COURIER)
                .toList();
        List<User> admins = isSuperAdmin() ? 
                searchResults.stream().filter(u -> u.getRole() == UserRole.ADMIN).toList() : 
                List.of();

        model.addAttribute("cooks", cooks);
        model.addAttribute("couriers", couriers);
        model.addAttribute("admins", admins);
        model.addAttribute("isSuperAdmin", isSuperAdmin());
        model.addAttribute("searchQuery", query);

        return "admin/staff/list";
    }

    // ============ СОЗДАНИЕ НОВОГО СОТРУДНИКА ============

    @GetMapping("/new")
    public String newStaffForm(Model model) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("roles", getAvailableRolesForCreation());
        model.addAttribute("isEdit", false);
        model.addAttribute("isSuperAdmin", isSuperAdmin());
        return "admin/staff/form";
    }

    @PostMapping("/save")
    public String saveStaff(@ModelAttribute("user") UserDto userDto,
                            RedirectAttributes redirectAttributes) {
        try {
            // Проверка: только ADMIN или SUPER_ADMIN может создавать ADMIN
            if (userDto.getRole() == UserRole.ADMIN) {
                UserRole currentRole = getCurrentUserRole();
                if (currentRole != UserRole.ADMIN && currentRole != UserRole.SUPER_ADMIN) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Недостаточно прав для создания админа");
                    return "redirect:/admin/staff";
                }
            }
            userService.createUser(userDto);
            redirectAttributes.addFlashAttribute("successMessage", "Сотрудник успешно создан");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при создании: " + e.getMessage());
        }
        return "redirect:/admin/staff";
    }

    // ============ РЕДАКТИРОВАНИЕ ============

    @GetMapping("/edit/{id}")
    public String editStaffForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);

        if (!isSuperAdmin() && user.getRole() == UserRole.ADMIN) {
            return "redirect:/admin/staff";
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFullName(user.getFullName());
        userDto.setPhone(user.getPhone());
        userDto.setRole(user.getRole());

        model.addAttribute("user", userDto);
        model.addAttribute("roles", getAvailableRolesForCreation());
        model.addAttribute("isEdit", true);
        model.addAttribute("isSuperAdmin", isSuperAdmin());

        return "admin/staff/form";
    }

    @PostMapping("/update/{id}")
    public String updateStaff(@PathVariable Long id,
                              @ModelAttribute("user") UserDto userDto,
                              RedirectAttributes redirectAttributes) {
        try {
            User existingUser = userService.getUserById(id);
            UserRole currentRole = getCurrentUserRole();

            // Проверка прав на редактирование админа
            if (existingUser.getRole() == UserRole.ADMIN) {
                if (currentRole != UserRole.ADMIN && currentRole != UserRole.SUPER_ADMIN) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Нет прав для редактирования админа");
                    return "redirect:/admin/staff";
                }
            }

            // Проверка прав на назначение роли ADMIN
            if (userDto.getRole() == UserRole.ADMIN) {
                if (currentRole != UserRole.ADMIN && currentRole != UserRole.SUPER_ADMIN) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Нет прав для назначения роли админа");
                    return "redirect:/admin/staff";
                }
            }

            userService.updateUser(id, userDto);
            redirectAttributes.addFlashAttribute("successMessage", "Данные сотрудника обновлены");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении: " + e.getMessage());
        }
        return "redirect:/admin/staff";
    }

    // ============ УДАЛЕНИЕ ============

    @PostMapping("/delete/{id}")
    public String deleteStaff(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id);
            UserRole currentRole = getCurrentUserRole();

            // Проверка прав на удаление админа
            if (user.getRole() == UserRole.ADMIN) {
                if (currentRole != UserRole.ADMIN && currentRole != UserRole.SUPER_ADMIN) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Нет прав для удаления админа");
                    return "redirect:/admin/staff";
                }
            }

            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Сотрудник удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении: " + e.getMessage());
        }
        return "redirect:/admin/staff";
    }

    // ============ БЛОКИРОВКА/АКТИВАЦИЯ ============

    @PostMapping("/toggle-status/{id}")
    public String toggleUserStatus(@PathVariable Long id,
                                   @RequestParam boolean active,
                                   RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id);
            UserRole currentRole = getCurrentUserRole();

            // Проверка прав: только ADMIN или SUPER_ADMIN может блокировать/активировать админов
            if (user.getRole() == UserRole.ADMIN) {
                if (currentRole != UserRole.ADMIN && currentRole != UserRole.SUPER_ADMIN) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Нет прав для изменения статуса админа");
                    return "redirect:/admin/staff";
                }
            }

            userService.updateUserStatus(id, active);
            String status = active ? "активирован" : "заблокирован";
            redirectAttributes.addFlashAttribute("successMessage",
                    "Сотрудник " + user.getFullName() + " " + status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        return "redirect:/admin/staff";
    }
}