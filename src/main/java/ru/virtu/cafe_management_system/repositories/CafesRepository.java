package ru.virtu.cafe_management_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.virtu.cafe_management_system.models.Cafe;

import java.util.Optional;

@Repository
public interface CafesRepository extends JpaRepository<Cafe, Long> {
    Optional<Cafe> findByName(String name);
}

