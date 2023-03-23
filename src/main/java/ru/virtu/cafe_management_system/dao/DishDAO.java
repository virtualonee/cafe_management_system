package ru.virtu.cafe_management_system.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.virtu.cafe_management_system.models.Dish;

import java.util.List;

@Component
public class DishDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DishDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Dish> showByCafeId(Long cafeId) {
        return jdbcTemplate.query("SELECT * FROM Dish WHERE cafe_id=?", new Object[]{cafeId}, new BeanPropertyRowMapper<>(Dish.class));
    }
}
