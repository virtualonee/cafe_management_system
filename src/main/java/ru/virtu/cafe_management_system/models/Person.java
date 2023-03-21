package ru.virtu.cafe_management_system.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Username shouldn't be empty")
    @Size(min = 2, max = 100, message = "Username should be from 2 to 100 symbols")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "Password shouldn't be empty")
    @Size(min = 2, max = 100, message = "Password should be from 2 to 100 symbols")
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "owner")
    List<Cafe> cafes;

    public Person(){

    }

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Cafe> getCafes() {
        return cafes;
    }

    public void setCafes(List<Cafe> cafes) {
        this.cafes = cafes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(username, person.username) && Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "Person{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
