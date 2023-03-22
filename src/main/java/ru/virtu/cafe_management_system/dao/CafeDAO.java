package ru.virtu.cafe_management_system.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.virtu.cafe_management_system.models.Cafe;

import java.util.List;

@Component
public class CafeDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CafeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Cafe> showByPersonId(Long personId) {
        return jdbcTemplate.query("SELECT * FROM Cafe WHERE person_id=?", new Object[]{personId}, new BeanPropertyRowMapper<>(Cafe.class));
    }
}
