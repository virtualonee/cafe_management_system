package ru.virtu.cafe_management_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtu.cafe_management_system.dao.DishDAO;
import ru.virtu.cafe_management_system.models.Dish;
import ru.virtu.cafe_management_system.repositories.DishRepository;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class DishesService {

    private final DishRepository dishRepository;
    private final DishDAO dishDAO;

    @Autowired
    public DishesService(DishRepository dishRepository, DishDAO dishDAO) {
        this.dishRepository = dishRepository;
        this.dishDAO = dishDAO;
    }

    public List<Dish> findAll() {
        return dishRepository.findAll();
    }

    public List<Dish> findByCafeId(Long cafeId) {
        return dishDAO.showByCafeId(cafeId);
    }

    public Dish findOne(Long id) {
        Optional<Dish> foundCafe = dishRepository.findById(id);
        return foundCafe.orElse(null);
    }

    @Transactional
    public void save(Dish cafe) {
        dishRepository.save(cafe);
    }

    @Transactional
    public void update(Long id, Dish updatedCafe) {
        updatedCafe.setId(id);
        dishRepository.save(updatedCafe);
    }

    @Transactional
    public void delete(Long id) {
        dishRepository.deleteById(id);
    }
}
