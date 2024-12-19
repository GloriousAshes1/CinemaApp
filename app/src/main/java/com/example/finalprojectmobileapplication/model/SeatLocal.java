package com.example.finalprojectmobileapplication.model;

import java.io.Serializable;

public class SeatLocal implements Serializable {

    private int id;
    private String title;
    private boolean selected;           //Trạng thái ghế được đặt
    private boolean checked;            //Trạng thái ghế được chọn
    private int roomId;
    private int timeId;

    public SeatLocal() {
    }

    public SeatLocal(int id, String title, boolean selected, int roomId, int timeId) {
        this.id = id;
        this.title = title;
        this.selected = selected;
        this.roomId = roomId;
        this.timeId = timeId;
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
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }
}
