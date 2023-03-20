package ru.virtu.cafe_management_system.models;

public class Shift {

    private Long id;
    private String timing;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }
}
