package com.example.finalprojectmobileapplication.model;

import java.io.Serializable;
import java.util.List;

public class TimeFirebase implements Serializable {
    private int id;
    private String title;
    private List<Seat> seats;

    public TimeFirebase() {
    }

    public TimeFirebase(int id, String title, List<Seat> seats) {
        this.id = id;
        this.title = title;
        this.seats = seats;
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

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
