package ru.virtu.cafe_management_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.virtu.cafe_management_system.models.Cafe;
import ru.virtu.cafe_management_system.services.CafesService;

import jakarta.validation.Valid;
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
        model.addAttribute("cafes", cafesService.findAll());
        return "cafes/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("cafes", cafesService.findOne(id));

        return "cafes/show";
    }

    @GetMapping("/new")
    public String newCafe(@ModelAttribute("cafes") Cafe cafe) {
        return "cafes/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("cafes") @Valid Cafe cafe,
                         BindingResult bindingResult) {
        cafeValidator.validate(cafe, bindingResult);

        if (bindingResult.hasErrors())
            return "cafes/new";

        cafesService.save(cafe);
        return "redirect:/cafes";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("cafe", cafesService.findOne(id));
        return "cafes/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("cafe") @Valid Cafe person, BindingResult bindingResult,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors())
            return "cafes/edit";

        cafesService.update(id, person);
        return "redirect:/cafes";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        cafesService.delete(id);
        return "redirect:/cafes";
    }
}
