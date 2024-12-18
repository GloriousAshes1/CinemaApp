package com.example.finalprojectmobileapplication.adapter.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.databinding.ItemRevenueBinding;
import com.example.finalprojectmobileapplication.model.Revenue;

import java.util.List;

public class AdminRevenueAdapter extends RecyclerView.Adapter<AdminRevenueAdapter.AdminRevenueViewHolder> {

    private final List<Revenue> mListRevenue;

    public AdminRevenueAdapter(List<Revenue> mListRevenue) {
        this.mListRevenue = mListRevenue;
    }

    @NonNull
    @Override
    public AdminRevenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRevenueBinding itemRevenueBinding = ItemRevenueBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminRevenueViewHolder(itemRevenueBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRevenueViewHolder holder, int position) {
        Revenue revenue = mListRevenue.get(position);
        if (revenue == null) {
            return;
        }
        holder.mItemRevenueBinding.tvStt.setText(String.valueOf(position + 1));
        holder.mItemRevenueBinding.tvMovieName.setText(revenue.getMovieName());
        holder.mItemRevenueBinding.tvQuantity.setText(String.valueOf(revenue.getQuantity()));
        String total = revenue.getTotalPrice() + ConstantKey.UNIT_CURRENCY;
        holder.mItemRevenueBinding.tvTotalPrice.setText(total);
    }

    @Override
    public int getItemCount() {
        if (mListRevenue != null) {
            return mListRevenue.size();
        }
        return 0;
    }

    public static class AdminRevenueViewHolder extends RecyclerView.ViewHolder {

        private final ItemRevenueBinding mItemRevenueBinding;

        public AdminRevenueViewHolder(@NonNull ItemRevenueBinding itemRevenueBinding) {
            super(itemRevenueBinding.getRoot());
            this.mItemRevenueBinding = itemRevenueBinding;
        }
    }
}
