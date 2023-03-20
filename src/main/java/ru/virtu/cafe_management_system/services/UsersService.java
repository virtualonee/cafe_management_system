package ru.virtu.cafe_management_system.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtu.cafe_management_system.models.Cafe;
import ru.virtu.cafe_management_system.models.User;
import ru.virtu.cafe_management_system.repositories.UsersRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> findAll() {
        return usersRepository.findAll();
    }

    public User findOne(Long id) {
        Optional<User> foundUser = usersRepository.findById(id);
        return foundUser.orElse(null);
    }

    @Transactional
    public void save(User user) {
        usersRepository.save(user);
    }

    @Transactional
    public void update(Long id, User updatedUser) {
        updatedUser.setId(id);
        usersRepository.save(updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        usersRepository.deleteById(id);
    }

    public List<Cafe> getCafesByUserId(Long id) {
        Optional<User> user = usersRepository.findById(id);

        if (user.isPresent()) {
            Hibernate.initialize(user.get().getCafes());

            return user.get().getCafes();
        }
        else {
            return Collections.emptyList();
        }
    }
}
