package ru.virtu.cafe_management_system.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.virtu.cafe_management_system.models.Cafe;
import ru.virtu.cafe_management_system.services.CafesService;


@Component
public class CafeValidator implements Validator {

    private final CafesService cafesService;

    @Autowired
    public CafeValidator(CafesService cafesService) {
        this.cafesService = cafesService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Cafe.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Cafe cafe = (Cafe) o;

        if (cafesService.getCafeByName(cafe.getName()).isPresent())
            errors.rejectValue("name", "", "Cafe with same name is already exist");
    }
}
