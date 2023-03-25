package ru.virtu.cafe_management_system.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.virtu.cafe_management_system.models.Booking;

import java.util.List;

@Component
public class BookingDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Booking> showByCafeId(Long cafeId) {
        return jdbcTemplate.query("SELECT * FROM Booking WHERE cafe_id=?", new Object[]{cafeId}, new BeanPropertyRowMapper<>(Booking.class));
    }
}
