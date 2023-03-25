package ru.virtu.cafe_management_system.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.virtu.cafe_management_system.models.CafeTable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class CafeTableDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CafeTableDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CafeTable> showByCafeId(Long cafeId) {
        return jdbcTemplate.query("SELECT * FROM cafe_table WHERE cafe_id=?", new Object[]{cafeId}, new BeanPropertyRowMapper<>(CafeTable.class));
    }

    public void createTables(Integer amount, Long cafeId) {
        jdbcTemplate.batchUpdate("INSERT INTO cafe_table(table_number, status, cafe_id) VALUES (?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, i+1);
                preparedStatement.setBoolean(2, false);
                preparedStatement.setLong(3, cafeId);
            }

            @Override
            public int getBatchSize() {
                return amount;
            }
        });
    }

    public void deleteAndCreateTables(Integer amount, Long cafeId) {
        jdbcTemplate.update("DELETE FROM cafe_table WHERE cafe_id = ?", new Object[]{cafeId});

        jdbcTemplate.batchUpdate("INSERT INTO cafe_table(table_number, status, cafe_id) VALUES (?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, i+1);
                preparedStatement.setBoolean(2, false);
                preparedStatement.setLong(3, cafeId);
            }

            @Override
            public int getBatchSize() {
                return amount;
            }
        });
    }
}
