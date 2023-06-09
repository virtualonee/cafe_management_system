package ru.virtu.cafe_management_system.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtu.cafe_management_system.dao.CafeDAO;
import ru.virtu.cafe_management_system.dao.CafeTableDAO;
import ru.virtu.cafe_management_system.models.Cafe;
import ru.virtu.cafe_management_system.models.Employee;
import ru.virtu.cafe_management_system.repositories.CafesRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class CafesService {

    private final CafesRepository cafesRepository;
    private final CafeDAO cafeDAO;
    private final CafeTableDAO cafeTableDAO;

    @Autowired
    public CafesService(CafesRepository cafesRepository, CafeDAO cafeDAO, CafeTableDAO cafeTableDAO) {
        this.cafesRepository = cafesRepository;
        this.cafeDAO = cafeDAO;
        this.cafeTableDAO = cafeTableDAO;
    }

    public List<Cafe> findAll() {
        return cafesRepository.findAll();
    }

    public List<Cafe> findByPersonId(Long personId) {
        return cafeDAO.showByPersonId(personId);
    }

    public Cafe findOne(Long id) {
        Optional<Cafe> foundCafe = cafesRepository.findById(id);
        return foundCafe.orElse(null);
    }

    @Transactional
    public void save(Cafe cafe) {
        cafesRepository.save(cafe);
        cafeTableDAO.createTables(cafe.getTableAmount(), cafe.getId());
    }

    @Transactional
    public void update(Long id, Cafe updatedCafe) {
        updatedCafe.setId(id);
        cafesRepository.save(updatedCafe);
        cafeTableDAO.deleteAndCreateTables(updatedCafe.getTableAmount(), updatedCafe.getId());
    }

    @Transactional
    public void delete(Long id) {
        cafesRepository.deleteById(id);
    }

    public Optional<Cafe> getCafeByName(String name) {
        return cafesRepository.findByName(name);
    }

//    public List<Employee> getEmployeeByCafeId(Long id) {
//        Optional<Cafe> cafe = cafesRepository.findById(id);
//
//        if (cafe.isPresent()) {
//            Hibernate.initialize(cafe.get().getEmployees());
//
//            return cafe.get().getEmployees();
//        }
//        else {
//            return Collections.emptyList();
//        }
//    }
}
