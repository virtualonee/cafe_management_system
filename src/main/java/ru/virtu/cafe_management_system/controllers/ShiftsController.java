package ru.virtu.cafe_management_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.virtu.cafe_management_system.models.Cafe;
import ru.virtu.cafe_management_system.models.Employee;
import ru.virtu.cafe_management_system.models.Shift;
import ru.virtu.cafe_management_system.security.PersonDetails;
import ru.virtu.cafe_management_system.services.CafesService;
import ru.virtu.cafe_management_system.services.ShiftsService;
import ru.virtu.cafe_management_system.services.PersonDetailsService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/shifts")
public class ShiftsController {

    private final ShiftsService shiftsService;
    private final CafesService cafesService;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public ShiftsController(ShiftsService shiftsService, CafesService cafesService, PersonDetailsService personDetailsService) {
        this.shiftsService = shiftsService;
        this.cafesService = cafesService;
        this.personDetailsService = personDetailsService;
    }

    @GetMapping()
    public String index(Model model, @CookieValue(value = "cafeId") String cafeIdCookie) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Long cafeId = Long.valueOf(cafeIdCookie);

        System.out.println(shiftsService.findByCafeId(cafeId));

        if (cafesService.findOne(cafeId).getOwner().equals(personDetails.getPerson())){

            model.addAttribute("shifts", shiftsService.findByCafeId(cafeId));
            model.addAttribute("cafeId", cafeId);

            return "shifts/index";
        }
        else {
            return "error/no_access";
        }


    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {

        Shift shift = shiftsService.findOne(id);

        if (isUserHaveRights(shift)){
            model.addAttribute("shift", shift);
            return "shifts/show";
        }
        else {
            return "error/no_access";
        }
    }

    @GetMapping("/new")
    public String newEmployee(@ModelAttribute("shift") Shift shift) {
        return "shifts/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("shifts") @Valid Shift shift, @CookieValue(value = "cafeId") String cafeId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Cafe cafe = cafesService.findOne(Long.valueOf(cafeId));

        List<Cafe> cafeList = cafesService.findByPersonId(personDetails.getPerson().getId());

        if (cafeList.contains(cafe)){
            shift.setCafe(cafe);
            shiftsService.save(shift);

            return "redirect:/shifts";
        }

        return "error/no_access";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {

        Shift shift = shiftsService.findOne(id);

        if (isUserHaveRights(shift)){
            model.addAttribute("shift", shift);
            return "shifts/edit";
        }
        else {
            return "error/no_access";
        }
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("shift") @Valid Shift shift, BindingResult bindingResult,
                         @PathVariable("id") Long id, @CookieValue(value = "cafeId") String cafeId) {
        if (bindingResult.hasErrors())
            return "shifts/edit";

        shift.setCafe(cafesService.findOne(Long.valueOf(cafeId)));

        shiftsService.update(id, shift);
        return "redirect:/shifts";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {

        Shift shift = shiftsService.findOne(id);

        if (isUserHaveRights(shift)){
            shiftsService.delete(id);
            return "redirect:/shifts";
        }
        else {
            return "error/no_access";
        }
    }

    public static Boolean isUserHaveRights(Shift shift){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        if (shift.getCafe().getOwner().equals(personDetails.getPerson())){
            return true;
        }
        else {
            return false;
        }

    }
}
