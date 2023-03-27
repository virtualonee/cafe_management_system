package ru.virtu.cafe_management_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.virtu.cafe_management_system.models.Cafe;
import ru.virtu.cafe_management_system.security.PersonDetails;
import ru.virtu.cafe_management_system.services.CafesService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import ru.virtu.cafe_management_system.util.CafeValidator;


@Controller
@RequestMapping("/cafes")
public class CafesController {

    private final CafesService cafesService;
    private final CafeValidator cafeValidator;

    @Autowired
    public CafesController(CafesService cafesService, CafeValidator cafeValidator) {
        this.cafesService = cafesService;
        this.cafeValidator = cafeValidator;
    }

    @GetMapping()
    public String index(Model model) {

        PersonDetails personDetails = (PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("cafes", cafesService.findByPersonId(personDetails.getPerson().getId()));

        return "cafes/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model, HttpServletResponse response) {

        Cafe cafe = cafesService.findOne(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        if (cafe.getOwner().equals(personDetails.getPerson())){
            model.addAttribute("cafe", cafe);

            Cookie cookieCafeId = new Cookie("cafeId", String.valueOf(id));
            cookieCafeId.setMaxAge(7*24*60*60);
            cookieCafeId.setPath("/");
            response.addCookie(cookieCafeId);

            return "cafes/show";
        }
        else {
            return "error/no_access";
        }
    }

    @GetMapping("/new")
    public String newCafe(@ModelAttribute("cafe") Cafe cafe) {
        return "cafes/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("cafes") @Valid Cafe cafe,
                         BindingResult bindingResult) {
        cafeValidator.validate(cafe, bindingResult);

        if (bindingResult.hasErrors())
            return "cafes/new";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        cafe.setOwner(personDetails.getPerson());

        cafesService.save(cafe);
        return "redirect:/cafes";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        Cafe cafe = cafesService.findOne(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        if (cafe.getOwner().equals(personDetails.getPerson())){
            model.addAttribute("cafe", cafe);
            return "cafes/edit";
        }
        else {
            return "error/no_access";
        }
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("cafe") @Valid Cafe cafe, BindingResult bindingResult,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors())
            return "cafes/edit";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        cafe.setOwner(personDetails.getPerson());

        cafesService.update(id, cafe);
        return "redirect:/cafes";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        Cafe cafe = cafesService.findOne(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        if (cafe.getOwner().equals(personDetails.getPerson())){
            cafesService.delete(id);
            return "redirect:/cafes";
        }
        else {
            return "error/no_access";
        }
    }
}
