package ru.top.kitchenmanager.Controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.kitchenmanager.dto.DishDto;
import ru.top.kitchenmanager.model.Dish;
import ru.top.kitchenmanager.service.DishService;

@Controller
@RequestMapping("/admin/dishes")
public class AdminDishController {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private DishService dishService;

    @GetMapping
    public String listDishes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("name").ascending());
        Page<Dish> dishPage;
        
        if (search != null && !search.trim().isEmpty()) {
            dishPage = dishService.getAllDishesPaginated(pageable);
            model.addAttribute("searchQuery", search);
        } else {
            dishPage = dishService.getAllDishesPaginated(pageable);
        }
        
        model.addAttribute("dishes", dishPage.getContent());
        model.addAttribute("dishPage", dishPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", dishPage.getTotalPages());
        
        return "admin/dishes/list";
    }

    @GetMapping("/new")
    public String newDishForm(Model model) {
        model.addAttribute("dish", new DishDto());
        model.addAttribute("isEdit", false);
        return "admin/dishes/form";
    }

    @PostMapping("/save")
    public String saveDish(
            @ModelAttribute("dish") DishDto dishDto,
            RedirectAttributes redirectAttributes) {
        try {
            dishService.createDish(dishDto);
            redirectAttributes.addFlashAttribute("successMessage", "Блюдо успешно создано");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        return "redirect:/admin/dishes";
    }

    @GetMapping("/edit/{id}")
    public String editDishForm(@PathVariable Long id, Model model) {
        Dish dish = dishService.getDishById(id);
        if (dish == null) {
            return "redirect:/admin/dishes";
        }
        
        DishDto dishDto = new DishDto();
        dishDto.setId(dish.getId());
        dishDto.setName(dish.getName());
        dishDto.setDescription(dish.getDescription());
        dishDto.setPrice(dish.getPrice());
        dishDto.setCategory(dish.getCategory());
        dishDto.setImageUrl(dish.getImageUrl());
        dishDto.setAvailable(dish.getAvailable());
        
        model.addAttribute("dish", dishDto);
        model.addAttribute("isEdit", true);
        return "admin/dishes/form";
    }

    @PostMapping("/update/{id}")
    public String updateDish(
            @PathVariable Long id,
            @ModelAttribute("dish") DishDto dishDto,
            RedirectAttributes redirectAttributes) {
        try {
            dishService.updateDish(id, dishDto);
            redirectAttributes.addFlashAttribute("successMessage", "Блюдо обновлено");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        return "redirect:/admin/dishes";
    }

    @PostMapping("/delete/{id}")
    public String deleteDish(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            dishService.deleteDish(id);
            redirectAttributes.addFlashAttribute("successMessage", "Блюдо удалено");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении: " + e.getMessage());
        }
        return "redirect:/admin/dishes";
    }

    @PostMapping("/toggle/{id}")
    public String toggleAvailability(
            @PathVariable Long id,
            @RequestParam boolean available,
            RedirectAttributes redirectAttributes) {
        try {
            Dish dish = dishService.getDishById(id);
            if (dish != null) {
                dish.setAvailable(available);
                dishService.saveDish(dish);
                String status = available ? "доступно" : "недоступно";
                redirectAttributes.addFlashAttribute("successMessage", "Блюдо теперь " + status);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        return "redirect:/admin/dishes";
    }
}
