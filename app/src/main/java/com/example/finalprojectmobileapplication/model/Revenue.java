package com.example.finalprojectmobileapplication.model;

import java.util.ArrayList;
import java.util.List;

public class Revenue {

    private long movieId;
    private String movieName;
    private List<BookingHistory> histories;

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public List<BookingHistory> getHistories() {
        if (histories == null) {
            histories = new ArrayList<>();
        }
        return histories;
    }

    public void setHistories(List<BookingHistory> histories) {
        this.histories = histories;
    }

    public int getQuantity() {
        if (histories == null || histories.isEmpty()) {
            return 0;
        }
        int result = 0;
        for (BookingHistory history : histories) {
            result += Integer.parseInt(history.getCount());
        }
        return result;
    }

    public int getTotalPrice() {
        if (histories == null || histories.isEmpty()) {
            return 0;
        }
        int result = 0;
        for (BookingHistory history : histories) {
            result += history.getTotal();
        }
        return result;
    }
}
