package ru.virtu.cafe_management_system.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.virtu.cafe_management_system.models.Employee;

import java.util.List;

@Component
public class EmployeeDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> showByCafeId(Long cafeId) {
        return jdbcTemplate.query("SELECT * FROM Employee WHERE cafe_id=?", new Object[]{cafeId}, new BeanPropertyRowMapper<>(Employee.class));
    }
}
