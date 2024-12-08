package com.example.finalprojectmobileapplication.model;

import java.io.Serializable;
import java.util.List;

public class RoomFirebase implements Serializable {
    private int id;
    private String title;
    private List<TimeFirebase> times;

    public RoomFirebase() {
    }

    public RoomFirebase(int id, String title, List<TimeFirebase> times) {
        this.id = id;
        this.title = title;
        this.times = times;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TimeFirebase> getTimes() {
        return times;
    }

    public void setTimes(List<TimeFirebase> times) {
        this.times = times;
    }
}
