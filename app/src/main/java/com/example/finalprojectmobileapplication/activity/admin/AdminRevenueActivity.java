package com.example.finalprojectmobileapplication.activity.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalprojectmobileapplication.MyApplication;
import com.example.finalprojectmobileapplication.adapter.admin.AdminRevenueAdapter;
import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.databinding.ActivityAdminRevenueBinding;
import com.example.finalprojectmobileapplication.model.BookingHistory;
import com.example.finalprojectmobileapplication.model.Revenue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminRevenueActivity extends AppCompatActivity {

    private ActivityAdminRevenueBinding mActivityAdminRevenueBinding;
    private List<Revenue> mListRevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAdminRevenueBinding = ActivityAdminRevenueBinding.inflate(getLayoutInflater());
        setContentView(mActivityAdminRevenueBinding.getRoot());

        initListener();
        getListRevenue();
    }

    private void initListener() {
        mActivityAdminRevenueBinding.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void getListRevenue() {
        MyApplication.get(this).getBookingDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<BookingHistory> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookingHistory bookingHistory = dataSnapshot.getValue(BookingHistory.class);
                    if (bookingHistory != null) {
                        list.add(bookingHistory);
                    }
                }
                handleDataHistories(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void handleDataHistories(List<BookingHistory> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        if (mListRevenue != null) {
            mListRevenue.clear();
        } else {
            mListRevenue = new ArrayList<>();
        }
        for (BookingHistory history : list) {
            long movieId = history.getMovieId();
            if (checkRevenueExist(movieId)) {
                getRevenueFromMovieId(movieId).getHistories().add(history);
            } else {
                Revenue revenue = new Revenue();
                revenue.setMovieId(history.getMovieId());
                revenue.setMovieName(history.getName());
                revenue.getHistories().add(history);
                mListRevenue.add(revenue);
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityAdminRevenueBinding.rcvData.setLayoutManager(linearLayoutManager);

        List<Revenue> listFinal = new ArrayList<>(mListRevenue);
        listFinal.sort((statistical1, statistical2)
                -> statistical2.getTotalPrice() - statistical1.getTotalPrice());
        AdminRevenueAdapter adminRevenueAdapter = new AdminRevenueAdapter(listFinal);
        mActivityAdminRevenueBinding.rcvData.setAdapter(adminRevenueAdapter);

        // Calculate total
        String strTotalValue = getTotalValues() + ConstantKey.UNIT_CURRENCY;
        mActivityAdminRevenueBinding.tvTotalValue.setText(strTotalValue);
    }

    private boolean checkRevenueExist(long movieId) {
        if (mListRevenue == null || mListRevenue.isEmpty()) {
            return false;
        }
        boolean result = false;
        for (Revenue revenue : mListRevenue) {
            if (movieId == revenue.getMovieId()) {
                result = true;
                break;
            }
        }
        return result;
    }

    private Revenue getRevenueFromMovieId(long movieId) {
        Revenue result = null;
        for (Revenue revenue : mListRevenue) {
            if (movieId == revenue.getMovieId()) {
                result = revenue;
                break;
            }
        }
        return result;
    }

    private int getTotalValues() {
        if (mListRevenue == null || mListRevenue.isEmpty()) {
            return 0;
        }

        int total = 0;
        for (Revenue revenue : mListRevenue) {
            total += revenue.getTotalPrice();
        }
        return total;
    }
}
