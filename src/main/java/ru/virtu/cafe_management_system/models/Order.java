package ru.virtu.cafe_management_system.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="cafe_order")
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment")
    private String comment;

    @ManyToMany
    @JoinTable(
            name = "dish_order",
            joinColumns = @JoinColumn(name = "cafe_order_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id"))
    private List<Dish> dishes;

    @Column(name = "made_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date madeAt;

    @ManyToOne
    @JoinColumn(name = "cafe_id", referencedColumnName = "id")
    private Cafe cafe;

    public Order() {

    }

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

    public Cafe getCafe() {
        return cafe;
    }

    public void setCafe(Cafe cafe) {
        this.cafe = cafe;
    }
}
