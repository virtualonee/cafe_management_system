package ru.virtu.cafe_management_system.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.virtu.cafe_management_system.models.Order;

import java.util.List;

@Component
public class OrderDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Order> showByCafeId(Long cafeId) {
        return jdbcTemplate.query("SELECT * FROM Cafe_order WHERE cafe_id=?", new Object[]{cafeId}, new BeanPropertyRowMapper<>(Order.class));
    }
}
