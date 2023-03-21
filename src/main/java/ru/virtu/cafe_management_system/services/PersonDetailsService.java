package ru.virtu.cafe_management_system.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.virtu.cafe_management_system.models.Cafe;
import ru.virtu.cafe_management_system.models.Person;
import ru.virtu.cafe_management_system.repositories.PersonRepository;
import ru.virtu.cafe_management_system.security.PersonDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUsername(s);

        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found!");

        return new PersonDetails(person.get());
    }

    public List<Cafe> getCafesByUserId(Long id) {
        Optional<Person> person = personRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getCafes());

            return person.get().getCafes();
        }
        else {
            return Collections.emptyList();
        }
    }
}
