package com.example.finalprojectmobileapplication.model;

public class BookingHistory {
    private long id;
    private long movieId;
    private String name;
    private String date;
    private String room;
    private String time;
    private String count;
    private String seats;
    private String foods;
    private String payment;
    private int total;
    private String user;
    private boolean used;

    public BookingHistory() {
    }

    public BookingHistory(long id, long movieId, String name, String date, String room, String time,
                          String count, String seats, String foods, String payment,
                          int total, String user, boolean used) {
        this.id = id;
        this.movieId = movieId;
        this.name = name;
        this.date = date;
        this.room = room;
        this.time = time;
        this.count = count;
        this.seats = seats;
        this.foods = foods;
        this.payment = payment;
        this.total = total;
        this.user = user;
        this.used = used;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getFoods() {
        return foods;
    }

    public void setFoods(String foods) {
        this.foods = foods;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
