package ru.virtu.cafe_management_system.controllers;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.virtu.cafe_management_system.models.Cafe;
import ru.virtu.cafe_management_system.models.Employee;
import ru.virtu.cafe_management_system.security.PersonDetails;
import ru.virtu.cafe_management_system.services.CafesService;
import ru.virtu.cafe_management_system.services.EmployeesService;
import ru.virtu.cafe_management_system.services.PersonDetailsService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/employees")
public class EmployeesController {

    private final EmployeesService employeesService;
    private final CafesService cafesService;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public EmployeesController(EmployeesService employeesService, CafesService cafesService, PersonDetailsService personDetailsService) {
        this.employeesService = employeesService;
        this.cafesService = cafesService;
        this.personDetailsService = personDetailsService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(required = false) Long cafeIdKey, @CookieValue(value = "cafeId") String cafeId, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        if (cafeIdKey != null){
            Cookie cookieCafeId = new Cookie("cafeId", String.valueOf(cafeIdKey));
            cookieCafeId.setMaxAge(7*24*60*60); // expires in 7 days
            response.addCookie(cookieCafeId);
        }
        else {
            cafeIdKey = Long.valueOf(cafeId);
        }

        if (cafesService.findOne(cafeIdKey).getOwner().equals(personDetails.getPerson())){

            model.addAttribute("employees", employeesService.findByCafeId(cafeIdKey));

            return "employees/index";
        }
        else {
            return "error/no_access";
        }


    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {

        Employee employee = employeesService.findOne(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        if (employee.getCafe().getOwner().equals(personDetails.getPerson())){
            model.addAttribute("employee", employee);
            return "employees/show";
        }
        else {
            return "error/no_access";
        }
    }

    @GetMapping("/new")
    public String newEmployee(@ModelAttribute("employee") Employee employee) {
        return "employees/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("employees") @Valid Employee employee, @CookieValue(value = "cafeId") String cafeId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Cafe cafe = cafesService.findOne(Long.valueOf(cafeId));

        List<Cafe> cafeList = cafesService.findByPersonId(personDetails.getPerson().getId());

        if (cafeList.contains(cafe)){
            employee.setCafe(cafe);
            employeesService.save(employee);

            return "redirect:/employees";
        }

        return "error/no_access";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {

        Employee employee = employeesService.findOne(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        if (employee.getCafe().getOwner().equals(personDetails.getPerson())){
            model.addAttribute("employee", employee);
            return "employees/edit";
        }
        else {
            return "error/no_access";
        }
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResult,
                         @PathVariable("id") Long id, @CookieValue(value = "cafeId") String cafeId) {
        if (bindingResult.hasErrors())
            return "employees/edit";

        employee.setCafe(cafesService.findOne(Long.valueOf(cafeId)));

        employeesService.update(id, employee);
        return "redirect:/employees";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Employee employee = employeesService.findOne(id);

        if (employee.getCafe().getOwner().equals(personDetails.getPerson())){
            employeesService.delete(id);
            return "redirect:/employees";
        }
        else {
            return "error/no_access";
        }
    }
}
