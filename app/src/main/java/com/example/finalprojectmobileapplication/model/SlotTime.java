package com.example.finalprojectmobileapplication.model;

public class SlotTime {
    private int id;
    private String title;
    private boolean isSelected;
    private int roomId;

    public SlotTime(int id, String title, boolean isSelected, int roomId) {
        this.id = id;
        this.title = title;
        this.isSelected = isSelected;
        this.roomId = roomId;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
