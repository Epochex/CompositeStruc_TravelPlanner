package com.traveloffersystem.business;

/**
 * Lieu 实体类
 */
public class Lieu {
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private String type; // plage, hotel, site_touristique, arret
    private int islandId;

    public Lieu() {}

    public Lieu(int id, String name, double latitude, double longitude, String type, int islandId) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.islandId = islandId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIslandId() {
        return islandId;
    }

    public void setIslandId(int islandId) {
        this.islandId = islandId;
    }
}
