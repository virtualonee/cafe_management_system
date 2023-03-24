package ru.virtu.cafe_management_system.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtu.cafe_management_system.dao.OrderDAO;
import ru.virtu.cafe_management_system.models.Dish;
import ru.virtu.cafe_management_system.models.Order;
import ru.virtu.cafe_management_system.repositories.DishRepository;
import ru.virtu.cafe_management_system.repositories.OrderRepository;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class OrdersService {

    private final OrderRepository orderRepository;
    private final OrderDAO orderDAO;
    private final DishRepository dishRepository;

    @Autowired
    public OrdersService(OrderRepository orderRepository, OrderDAO orderDAO, DishRepository dishRepository) {
        this.orderRepository = orderRepository;
        this.orderDAO = orderDAO;
        this.dishRepository = dishRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findByCafeId(Long cafeId) {
        return orderDAO.showByCafeId(cafeId);
    }

    public Order findOne(Long id) {
        Optional<Order> foundCafe = orderRepository.findById(id);
        return foundCafe.orElse(null);
    }

    @Transactional
    public void save(Order cafe) {
        orderRepository.save(cafe);
    }

    @Transactional
    public void update(Long id, Order updatedCafe) {
        updatedCafe.setId(id);
        orderRepository.save(updatedCafe);
    }

    @Transactional
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    public void addDish(Long id, Dish dish) {

        Optional<Order> order = orderRepository.findById(id);
        List<Dish> dishes =  order.get().getDishes();

        dishes.add(dish);
        order.get().setDishes(dishes);

        orderRepository.save(order.get());
    }

    @Transactional
    public void deleteDish(Long dishId, Long orderId) {

        Optional<Order> order = orderRepository.findById(orderId);
        Hibernate.initialize(order.get().getDishes());

        List<Dish> dishes =  order.get().getDishes();

        Dish dishToRemove = dishRepository.getById(dishId);

        dishes.remove(dishToRemove);
        order.get().setDishes(dishes);

        orderRepository.save(order.get());
    }

    public List<Dish> getDishes(Order order) {
        Hibernate.initialize(order.getDishes());
        return order.getDishes();
    }
}
