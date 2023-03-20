package ru.virtu.cafe_management_system.models;

import java.util.Date;
import java.util.List;

public class Order {

    private Long id;
    private String comment;
    private List<Dish> dishes;
    private Date madeAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public Date getMadeAt() {
        return madeAt;
    }

    public void setMadeAt(Date madeAt) {
        this.madeAt = madeAt;
    }
}
