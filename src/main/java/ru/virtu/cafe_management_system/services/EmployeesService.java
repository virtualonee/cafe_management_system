package ru.virtu.cafe_management_system.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtu.cafe_management_system.dao.EmployeeDAO;
import ru.virtu.cafe_management_system.models.Employee;
import ru.virtu.cafe_management_system.models.Shift;
import ru.virtu.cafe_management_system.repositories.CafesRepository;
import ru.virtu.cafe_management_system.repositories.EmployeeRepository;
import ru.virtu.cafe_management_system.repositories.ShiftRepository;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class EmployeesService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeDAO employeeDAO;
    private final ShiftRepository shiftRepository;

    @Autowired
    public EmployeesService(EmployeeRepository employeeRepository, EmployeeDAO employeeDAO, ShiftRepository shiftRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeDAO = employeeDAO;
        this.shiftRepository = shiftRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public List<Employee> findByCafeId(Long cafeId) {

        return employeeDAO.showByCafeId(cafeId);
    }

    public Employee findOne(Long id) {
        Optional<Employee> foundCafe = employeeRepository.findById(id);
        return foundCafe.orElse(null);
    }

    @Transactional
    public void save(Employee cafe) {
        employeeRepository.save(cafe);
    }

    @Transactional
    public void update(Long id, Employee updatedCafe) {
        updatedCafe.setId(id);
        employeeRepository.save(updatedCafe);
    }

    @Transactional
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    @Transactional
    public void assignShift(Employee employee, Long shiftId){
        Shift shift = shiftRepository.getById(shiftId);
        employee.setShift(shift);
        employeeRepository.save(employee);

    }
}
