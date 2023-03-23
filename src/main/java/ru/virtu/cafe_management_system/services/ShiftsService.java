package ru.virtu.cafe_management_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtu.cafe_management_system.dao.EmployeeDAO;
import ru.virtu.cafe_management_system.dao.ShiftDAO;
import ru.virtu.cafe_management_system.models.Shift;
import ru.virtu.cafe_management_system.repositories.ShiftRepository;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class ShiftsService {

    private final ShiftRepository shiftRepository;
    private final ShiftDAO shiftDAO;

    @Autowired
    public ShiftsService(ShiftRepository shiftRepository, ShiftDAO shiftDAO) {
        this.shiftRepository = shiftRepository;
        this.shiftDAO = shiftDAO;
    }

    public List<Shift> findAll() {
        return shiftRepository.findAll();
    }

    public List<Shift> findByCafeId(Long cafeId) {
        return shiftDAO.showByCafeId(cafeId);
    }

    public Shift findOne(Long id) {
        Optional<Shift> foundCafe = shiftRepository.findById(id);
        return foundCafe.orElse(null);
    }

    @Transactional
    public void save(Shift cafe) {
        shiftRepository.save(cafe);
    }

    @Transactional
    public void update(Long id, Shift updatedCafe) {
        updatedCafe.setId(id);
        shiftRepository.save(updatedCafe);
    }

    @Transactional
    public void delete(Long id) {
        shiftRepository.deleteById(id);
    }
}
