package ru.virtu.cafe_management_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.virtu.cafe_management_system.models.Cafe;
import ru.virtu.cafe_management_system.models.Dish;
import ru.virtu.cafe_management_system.models.Shift;
import ru.virtu.cafe_management_system.security.PersonDetails;
import ru.virtu.cafe_management_system.services.CafesService;
import ru.virtu.cafe_management_system.services.DishesService;
import ru.virtu.cafe_management_system.services.ShiftsService;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/dishes")
public class DishesController {

    private final DishesService dishesService;
    private final CafesService cafesService;

    @Autowired
    public DishesController(DishesService dishesService, CafesService cafesService) {
        this.dishesService = dishesService;
        this.cafesService = cafesService;
    }

    @GetMapping()
    public String index(Model model, @CookieValue(value = "cafeId") String cafeIdCookie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Long cafeId = Long.valueOf(cafeIdCookie);

        if (cafesService.findOne(cafeId).getOwner().equals(personDetails.getPerson())){
            model.addAttribute("dishes", dishesService.findByCafeId(cafeId));
            model.addAttribute("cafeId", cafeId);

            return "dishes/index";
        }
        else {
            return "error/no_access";
        }


    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Dish dish = dishesService.findOne(id);

        if (isUserHaveRights(dish)){
            model.addAttribute("dish", dish);

            return "dishes/show";
        }
        else {
            return "error/no_access";
        }
    }

    @GetMapping("/new")
    public String newEmployee(@ModelAttribute("dish") Dish dish) {
        return "dishes/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("dishes") @Valid Dish dish, @CookieValue(value = "cafeId") String cafeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Cafe cafe = cafesService.findOne(Long.valueOf(cafeId));

        List<Cafe> cafeList = cafesService.findByPersonId(personDetails.getPerson().getId());

        if (cafeList.contains(cafe)){
            dish.setCafe(cafe);
            dishesService.save(dish);

            return "redirect:/dishes";
        }

        return "error/no_access";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        Dish dish = dishesService.findOne(id);

        if (isUserHaveRights(dish)){
            model.addAttribute("dish", dish);
            return "dishes/edit";
        }
        else {
            return "error/no_access";
        }
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("dish") @Valid Dish dish, BindingResult bindingResult,
                         @PathVariable("id") Long id, @CookieValue(value = "cafeId") String cafeId) {
        if (bindingResult.hasErrors())
            return "dishes/edit";

        dish.setCafe(cafesService.findOne(Long.valueOf(cafeId)));

        if (isUserHaveRights(dish)){
            dishesService.update(id, dish);
            return "redirect:/dishes";
        }
        else {
            return "error/no_access";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        Dish dish = dishesService.findOne(id);

        if (isUserHaveRights(dish)){
            dishesService.delete(id);
            return "redirect:/dishes";
        }
        else {
            return "error/no_access";
        }
    }

    public static Boolean isUserHaveRights(Dish dish){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        if (dish.getCafe().getOwner().equals(personDetails.getPerson())){
            return true;
        }
        else {
            return false;
        }

    }
}
