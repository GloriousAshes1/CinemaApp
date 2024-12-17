package com.example.finalprojectmobileapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.databinding.ItemBookingHistoryBinding;
import com.example.finalprojectmobileapplication.listener.IOnSingleClickListener;
import com.example.finalprojectmobileapplication.model.BookingHistory;
import com.example.finalprojectmobileapplication.util.DateTimeUtils;

import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.BookingHistoryViewHolder> {

    private Context mContext;
    private final List<BookingHistory> mListBookingHistory;
    private final IClickQRListener iClickQRListener;
    private final IClickConfirmListener iClickConfirmListener;
    private final boolean mIsAdmin;

    public interface IClickQRListener {
        void onClickOpenQrCode(String id);
    }

    public interface IClickConfirmListener {
        void onClickConfirmBooking(String id);
    }

    public BookingHistoryAdapter(Context context, boolean isAdmin,
                                 List<BookingHistory> mListBookingHistory, IClickQRListener listener, IClickConfirmListener confirmListener) {
        this.mContext = context;
        this.mIsAdmin = isAdmin;
        this.mListBookingHistory = mListBookingHistory;
        this.iClickQRListener = listener;
        this.iClickConfirmListener = confirmListener;
    }

    @NonNull
    @Override
    public BookingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookingHistoryBinding itemBookingHistoryBinding = ItemBookingHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BookingHistoryViewHolder(itemBookingHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHistoryViewHolder holder, int position) {
        BookingHistory bookingHistory = mListBookingHistory.get(position);
        if (bookingHistory == null) {
            return;
        }
        boolean isExpire = DateTimeUtils.convertDateToTimeStamp(bookingHistory.getDate()) < DateTimeUtils.getLongCurrentTimeStamp();
        if (isExpire || bookingHistory.isUsed()) {
            holder.mItemBookingHistoryBinding.layoutItem.setBackgroundColor(mContext.getResources().getColor(R.color.black_overlay));
        } else {
            holder.mItemBookingHistoryBinding.layoutItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        holder.mItemBookingHistoryBinding.tvId.setText(String.valueOf(bookingHistory.getId()));
        holder.mItemBookingHistoryBinding.tvNameMovie.setText(bookingHistory.getName());
        holder.mItemBookingHistoryBinding.tvDateMovie.setText(bookingHistory.getDate());
        holder.mItemBookingHistoryBinding.tvRoomMovie.setText(bookingHistory.getRoom());
        holder.mItemBookingHistoryBinding.tvTimeMovie.setText(bookingHistory.getTime());
        holder.mItemBookingHistoryBinding.tvCountBooking.setText(bookingHistory.getCount());
        holder.mItemBookingHistoryBinding.tvCountSeat.setText(bookingHistory.getSeats());
        holder.mItemBookingHistoryBinding.tvFoodDrink.setText(bookingHistory.getFoods());
        holder.mItemBookingHistoryBinding.tvPaymentMethod.setText(bookingHistory.getPayment());
        String strTotal = bookingHistory.getTotal() + ConstantKey.UNIT_CURRENCY;
        holder.mItemBookingHistoryBinding.tvTotalAmount.setText(strTotal);
        holder.mItemBookingHistoryBinding.tvDateCreate.setText(DateTimeUtils.convertTimeStampToDate(String.valueOf(bookingHistory.getId())));

        if (mIsAdmin) {
            holder.mItemBookingHistoryBinding.imgQr.setVisibility(View.GONE);
            holder.mItemBookingHistoryBinding.layoutEmail.setVisibility(View.VISIBLE);
            holder.mItemBookingHistoryBinding.tvEmail.setText(bookingHistory.getUser());
            if (isExpire || bookingHistory.isUsed()) {
                holder.mItemBookingHistoryBinding.layoutConfirm.setVisibility(View.GONE);
            } else {
                holder.mItemBookingHistoryBinding.layoutConfirm.setVisibility(View.VISIBLE);
                holder.mItemBookingHistoryBinding.chbConfirm.setOnClickListener(new IOnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        iClickConfirmListener.onClickConfirmBooking(String.valueOf(bookingHistory.getId()));
                    }
                });
            }
        } else {
            holder.mItemBookingHistoryBinding.layoutConfirm.setVisibility(View.GONE);
            holder.mItemBookingHistoryBinding.layoutEmail.setVisibility(View.GONE);
            holder.mItemBookingHistoryBinding.imgQr.setVisibility(View.VISIBLE);
            if (isExpire || bookingHistory.isUsed()) {
                holder.mItemBookingHistoryBinding.imgQr.setOnClickListener(null);
            } else {
                holder.mItemBookingHistoryBinding.imgQr.setOnClickListener(new IOnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        iClickQRListener.onClickOpenQrCode(String.valueOf(bookingHistory.getId()));
                    }
                });
            }
        }
    }

    public void release() {
        mContext = null;
    }

    @Override
    public int getItemCount() {
        if (mListBookingHistory != null) {
            return mListBookingHistory.size();
        }
        return 0;
    }

    public static class BookingHistoryViewHolder extends RecyclerView.ViewHolder {

        private final ItemBookingHistoryBinding mItemBookingHistoryBinding;

        public BookingHistoryViewHolder(@NonNull ItemBookingHistoryBinding itemBookingHistoryBinding) {
            super(itemBookingHistoryBinding.getRoot());
            this.mItemBookingHistoryBinding = itemBookingHistoryBinding;
        }
    }
}