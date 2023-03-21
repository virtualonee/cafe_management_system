package ru.virtu.cafe_management_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.virtu.cafe_management_system.models.Cafe;
import ru.virtu.cafe_management_system.models.Employee;
import ru.virtu.cafe_management_system.services.EmployeesService;
import ru.virtu.cafe_management_system.util.CafeValidator;

import javax.validation.Valid;


@Controller
@RequestMapping("/employees")
public class EmployeesController {

    private final EmployeesService employeesService;

    @Autowired
    public EmployeesController(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @GetMapping()
    public String index(Model model) {

        model.addAttribute("employees", employeesService.findByCafeId(0l));

        return "employees/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("employee", employeesService.findOne(id));

        return "employees/show";
    }

    @GetMapping("/new")
    public String newCafe(@ModelAttribute("cafe") Cafe cafe) {
        return "employees/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("employees") @Valid Employee employee) {

        employeesService.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("employee", employeesService.findOne(id));
        return "employees/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResult,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors())
            return "employees/edit";

        employeesService.update(id, employee);
        return "redirect:/employees";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        employeesService.delete(id);
        return "redirect:/employees";
    }
}
