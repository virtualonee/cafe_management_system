package ru.virtu.cafe_management_system.models;

public class Reservation {

    private Long id;
    private Integer tableNumber;
    private Integer peopleAmount;
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Integer getPeopleAmount() {
        return peopleAmount;
    }

    public void setPeopleAmount(Integer peopleAmount) {
        this.peopleAmount = peopleAmount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
