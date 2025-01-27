package com.traveloffersystem.business;

/**
 * Hotel 实体类
 */
public class Hotel {
    private int id;
    private String description;
    private int stars; // 1 到 5 之间
    private int plageId; // 外键，指向 Plage 表

    public Hotel() {}

    public Hotel(int id, String description, int stars, int plageId) {
        this.id = id;
        this.description = description;
        this.stars = stars;
        this.plageId = plageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getPlageId() {
        return plageId;
    }

    public void setPlageId(int plageId) {
        this.plageId = plageId;
    }
}
