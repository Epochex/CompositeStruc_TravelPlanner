package com.traveloffersystem.business;
/**
 * SiteTouristique 实体类
 */
public class SiteTouristique {
    private int id;
    private int duration; // 持续时间
    private String type; // site_historique, site_activite

    public SiteTouristique() {}

    public SiteTouristique(int id, int duration, String type) {
        this.id = id;
        this.duration = duration;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
