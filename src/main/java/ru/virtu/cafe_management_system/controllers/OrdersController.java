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
import ru.virtu.cafe_management_system.models.Order;
import ru.virtu.cafe_management_system.security.PersonDetails;
import ru.virtu.cafe_management_system.services.CafesService;
import ru.virtu.cafe_management_system.services.DishesService;
import ru.virtu.cafe_management_system.services.PersonDetailsService;
import ru.virtu.cafe_management_system.services.OrdersService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@Controller
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService ordersService;
    private final CafesService cafesService;
    private final DishesService dishesService;

    @Autowired
    public OrdersController(OrdersService ordersService, CafesService cafesService, DishesService dishesService) {
        this.ordersService = ordersService;
        this.cafesService = cafesService;
        this.dishesService = dishesService;
    }

    @GetMapping()
    public String index(Model model, @CookieValue(value = "cafeId") String cafeIdCookie) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Long cafeId = Long.valueOf(cafeIdCookie);

        System.out.println(ordersService.findByCafeId(cafeId));

        if (cafesService.findOne(cafeId).getOwner().equals(personDetails.getPerson())){

            model.addAttribute("orders", ordersService.findByCafeId(cafeId));
            model.addAttribute("cafeId", cafeId);

            return "orders/index";
        }
        else {
            return "error/no_access";
        }


    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model, @ModelAttribute("addedDish") Dish dish) {

        Order order = ordersService.findOne(id);

        if (isUserHaveRights(order)){
            model.addAttribute("orderedDishes", ordersService.getDishes(order));
            model.addAttribute("order", order);
            model.addAttribute("allDishes", dishesService.findByCafeId(order.getCafe().getId()));
            return "orders/show";
        }
        else {
            return "error/no_access";
        }
    }

    @GetMapping("/new")
    public String newEmployee(@ModelAttribute("order") Order order) {
        return "orders/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("orders") @Valid Order order, @CookieValue(value = "cafeId") String cafeId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Cafe cafe = cafesService.findOne(Long.valueOf(cafeId));

        List<Cafe> cafeList = cafesService.findByPersonId(personDetails.getPerson().getId());

        if (cafeList.contains(cafe)){

            order.setMadeAt(new Date());
            order.setCafe(cafe);
            ordersService.save(order);

            return "redirect:/orders";
        }

        return "error/no_access";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {

        Order order = ordersService.findOne(id);

        if (isUserHaveRights(order)){
            model.addAttribute("order", order);
            return "orders/edit";
        }
        else {
            return "error/no_access";
        }
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("order") @Valid Order order, BindingResult bindingResult,
                         @PathVariable("id") Long id, @CookieValue(value = "cafeId") String cafeId) {
        if (bindingResult.hasErrors())
            return "orders/edit";

        order.setCafe(cafesService.findOne(Long.valueOf(cafeId)));

        ordersService.update(id, order);
        return "redirect:/orders";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {

        Order order = ordersService.findOne(id);

        if (isUserHaveRights(order)){
            ordersService.delete(id);
            return "redirect:/orders";
        }
        else {
            return "error/no_access";
        }
    }

    @PatchMapping("/{id}/addDish")
    public String addDish(@PathVariable("id") Long id,@ModelAttribute("addedDish") Dish dish, @ModelAttribute("order") @Valid Order order, @CookieValue(value = "cafeId") String cafeId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Cafe cafe = cafesService.findOne(Long.valueOf(cafeId));

        List<Cafe> cafeList = cafesService.findByPersonId(personDetails.getPerson().getId());

        if (cafeList.contains(cafe)){
            ordersService.addDish(id, dish);

            return "redirect:/orders/"+id;
        }

        return "error/no_access";
    }

    @DeleteMapping("/deleteDish/{id}")
    public String deleteDish(@PathVariable("id") Long dishId, @RequestParam Long orderId) {

            ordersService.deleteDish(dishId, orderId);

            return "redirect:/orders/"+orderId;
    }

    public static Boolean isUserHaveRights(Order order){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        if (order.getCafe().getOwner().equals(personDetails.getPerson())){
            return true;
        }
        else {
            return false;
        }

    }
}
