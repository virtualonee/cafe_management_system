package ru.virtu.cafe_management_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.virtu.cafe_management_system.models.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
}

