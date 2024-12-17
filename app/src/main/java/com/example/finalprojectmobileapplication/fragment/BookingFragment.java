package com.example.finalprojectmobileapplication.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.finalprojectmobileapplication.MyApplication;
import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.adapter.BookingHistoryAdapter;
import com.example.finalprojectmobileapplication.constant.GlobalFunction;
import com.example.finalprojectmobileapplication.databinding.FragmentBookingBinding;
import com.example.finalprojectmobileapplication.listener.IOnSingleClickListener;
import com.example.finalprojectmobileapplication.model.BookingHistory;
import com.example.finalprojectmobileapplication.prefs.DataStoreManager;
import com.example.finalprojectmobileapplication.util.DateTimeUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {

    private FragmentBookingBinding mFragmentBookingBinding;
    private List<BookingHistory> mListBookingHistory;
    private BookingHistoryAdapter mBookingHistoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentBookingBinding = FragmentBookingBinding.inflate(inflater, container, false);

        getListBookingHistory(false);
        mFragmentBookingBinding.chbBookingUsed.setOnCheckedChangeListener((buttonView, isChecked) -> getListBookingHistory(isChecked));
        return mFragmentBookingBinding.getRoot();
    }

    public void getListBookingHistory(boolean isUsed) {
        if (getActivity() == null) {
            return;
        }
        MyApplication.get(getActivity()).getBookingDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListBookingHistory != null) {
                    mListBookingHistory.clear();
                } else {
                    mListBookingHistory = new ArrayList<>();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookingHistory bookingHistory = dataSnapshot.getValue(BookingHistory.class);
                    if (bookingHistory != null
                            && DataStoreManager.getUser().getEmail().equals(bookingHistory.getUser())) {
                        boolean isExpire = DateTimeUtils.convertDateToTimeStamp(bookingHistory.getDate()) < DateTimeUtils.getLongCurrentTimeStamp();
                        if (isUsed) {
                            if (isExpire || bookingHistory.isUsed()) {
                                mListBookingHistory.add(0, bookingHistory);
                            }
                        } else {
                            if (!isExpire && !bookingHistory.isUsed()) {
                                mListBookingHistory.add(0, bookingHistory);
                            }
                        }
                    }
                }

                displayListBookingHistory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void displayListBookingHistory() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentBookingBinding.rcvBookingHistory.setLayoutManager(linearLayoutManager);

        mBookingHistoryAdapter = new BookingHistoryAdapter(getActivity(), false,
                mListBookingHistory, this::showDialogConfirmBooking, null);
        mFragmentBookingBinding.rcvBookingHistory.setAdapter(mBookingHistoryAdapter);
    }

    private void showDialogConfirmBooking(String id) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_qr_code);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        // Get view
        final ImageView imgClose = dialog.findViewById(R.id.img_close);
        final ImageView imgQrCode = dialog.findViewById(R.id.img_qr_code);

        // Set data
        GlobalFunction.gentQRCodeFromString(imgQrCode, id);

        // Set listener
        imgClose.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBookingHistoryAdapter != null) mBookingHistoryAdapter.release();
    }
}
