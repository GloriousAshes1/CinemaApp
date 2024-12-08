package com.example.finalprojectmobileapplication.model;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {
    private long id;
    private String name;
    private String description;
    private int price;
    private String date;
    private String image;
    private String imageBanner;
    private String url;
    private List<RoomFirebase> rooms;
    private long categoryId;
    private String categoryName;
    private int booked;

    public Movie() {
    }

    public Movie(long id, String name, String description, int price, String date, String image, String imageBanner, String url, List<RoomFirebase> rooms, long categoryId, String categoryName, int booked) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.date = date;
        this.image = image;
        this.imageBanner = imageBanner;
        this.url = url;
        this.rooms = rooms;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.booked = booked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageBanner() {
        return imageBanner;
    }

    public void setImageBanner(String imageBanner) {
        this.imageBanner = imageBanner;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<RoomFirebase> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomFirebase> rooms) {
        this.rooms = rooms;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getBooked() {
        return booked;
    }

    public void setBooked(int booked) {
        this.booked = booked;
    }
}
