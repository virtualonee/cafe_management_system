package ru.virtu.cafe_management_system.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.virtu.cafe_management_system.models.Shift;

import java.util.List;

@Component
public class ShiftDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ShiftDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Shift> showByCafeId(Long cafeId) {
        return jdbcTemplate.query("SELECT * FROM Shift WHERE cafe_id=?", new Object[]{cafeId}, new BeanPropertyRowMapper<>(Shift.class));
    }
}
