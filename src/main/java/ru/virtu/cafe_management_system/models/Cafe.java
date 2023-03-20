package ru.virtu.cafe_management_system.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="Cafe")
public class Cafe {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Cafe name shouldn't be empty")
    @Size(min = 2, max = 100, message = "Cafe name should be from 2 to 100 symbols")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Address shouldn't be empty")
    @Size(min = 2, max = 100, message = "Address should be from 2 to 100 symbols")
    @Column(name = "address")
    private String address;

    @Column(name = "places")
    private Integer places;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    private List<Employee> employees;
    private List<Dish> dishes;
    private List<Order> orders;
    private List<Reservation> tableOrders;

    public Cafe(){

    }

    public Cafe(String name, String address, Integer places, User owner) {
        this.name = name;
        this.address = address;
        this.places = places;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPlaces() {
        return places;
    }

    public void setPlaces(Integer places) {
        this.places = places;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Reservation> getTableOrders() {
        return tableOrders;
    }

    public void setTableOrders(List<Reservation> tableOrders) {
        this.tableOrders = tableOrders;
    }
}
