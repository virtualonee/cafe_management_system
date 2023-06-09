package ru.virtu.cafe_management_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.virtu.cafe_management_system.models.Cafe;
import ru.virtu.cafe_management_system.models.Booking;
import ru.virtu.cafe_management_system.models.CafeTable;
import ru.virtu.cafe_management_system.security.PersonDetails;
import ru.virtu.cafe_management_system.services.CafeTablesService;
import ru.virtu.cafe_management_system.services.CafesService;
import ru.virtu.cafe_management_system.services.BookingsService;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/bookings")
public class BookingsController {

    private final BookingsService bookingsService;
    private final CafesService cafesService;
    private final CafeTablesService cafeTablesService;

    @Autowired
    public BookingsController(BookingsService bookingsService, CafesService cafesService, CafeTablesService cafeTablesService) {
        this.bookingsService = bookingsService;
        this.cafesService = cafesService;
        this.cafeTablesService = cafeTablesService;
    }

    @GetMapping()
    public String index(Model model, @CookieValue(value = "cafeId") String cafeIdCookie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Long cafeId = Long.valueOf(cafeIdCookie);

        if (cafesService.findOne(cafeId).getOwner().equals(personDetails.getPerson())){
            model.addAttribute("bookings", bookingsService.findByCafeId(cafeId));
            model.addAttribute("cafeId", cafeId);

            return "bookings/index";
        }
        else {
            return "error/no_access";
        }


    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Booking booking = bookingsService.findOne(id);

        if (isUserHaveRights(booking)){
            model.addAttribute("booking", booking);

            return "bookings/show";
        }
        else {
            return "error/no_access";
        }
    }

    @GetMapping("/new")
    public String newEmployee(@ModelAttribute("booking") Booking booking, Model model, @CookieValue(value = "cafeId") String cafeId) {

        List<CafeTable> freeTables = cafeTablesService.showFreeTablesByCafeId(Long.valueOf(cafeId));

        if (freeTables != null){
            model.addAttribute("freeTables", freeTables);
            return "bookings/new";
        }
        else {
            return "error/no_free_tables";
        }

    }

    @PostMapping()
    public String create(@ModelAttribute("bookings") @Valid Booking booking, @CookieValue(value = "cafeId") String cafeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Cafe cafe = cafesService.findOne(Long.valueOf(cafeId));

        List<Cafe> cafeList = cafesService.findByPersonId(personDetails.getPerson().getId());

        if (cafeList.contains(cafe)){
            cafeTablesService.bookingTable(booking.getTableNumber());
            booking.setCafe(cafe);
            booking.setMadeAt(new Date());
            bookingsService.save(booking);

            return "redirect:/bookings";
        }

        return "error/no_access";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id, @CookieValue(value = "cafeId") String cafeId) {
        Booking booking = bookingsService.findOne(id);

        if (isUserHaveRights(booking)){
            model.addAttribute("booking", booking);
            model.addAttribute("freeTables", cafeTablesService.findByCafeId(Long.valueOf(cafeId)));
            return "bookings/edit";
        }
        else {
            return "error/no_access";
        }
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("booking") @Valid Booking booking, BindingResult bindingResult,
                         @PathVariable("id") Long id, @CookieValue(value = "cafeId") String cafeId, @RequestParam Integer oldTableNumber) {
        if (bindingResult.hasErrors())
            return "bookings/edit";

        booking.setCafe(cafesService.findOne(Long.valueOf(cafeId)));

        if (isUserHaveRights(booking)){
            cafeTablesService.unbookingTable(oldTableNumber);
            cafeTablesService.bookingTable(booking.getTableNumber());
            bookingsService.update(id, booking);
            return "redirect:/bookings";
        }
        else {
            return "error/no_access";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        Booking booking = bookingsService.findOne(id);

        if (isUserHaveRights(booking)){
            bookingsService.delete(id);
            return "redirect:/bookings";
        }
        else {
            return "error/no_access";
        }
    }

    public static Boolean isUserHaveRights(Booking booking){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        if (booking.getCafe().getOwner().equals(personDetails.getPerson())){
            return true;
        }
        else {
            return false;
        }

    }
}
