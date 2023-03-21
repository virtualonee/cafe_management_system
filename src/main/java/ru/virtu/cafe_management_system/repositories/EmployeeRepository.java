package ru.virtu.cafe_management_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.virtu.cafe_management_system.models.Employee;
import ru.virtu.cafe_management_system.models.Person;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

