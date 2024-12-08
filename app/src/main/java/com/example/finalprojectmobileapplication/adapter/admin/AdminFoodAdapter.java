package com.example.finalprojectmobileapplication.adapter.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.databinding.ItemFoodBinding;
import com.example.finalprojectmobileapplication.model.Food;

import java.util.List;

public class AdminFoodAdapter extends RecyclerView.Adapter<AdminFoodAdapter.FoodViewHolder> {
    private final List<Food> foodList;
    private final IManagerFoodListener iManagerFoodListener;

    public interface IManagerFoodListener{
        void editFood(Food food);

        void deleteFood(Food food);
    }

    public AdminFoodAdapter(List<Food> foodList, IManagerFoodListener iManagerFoodListener){
        this.foodList = foodList;
        this.iManagerFoodListener = iManagerFoodListener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodBinding itemFoodBinding = ItemFoodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodViewHolder(itemFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        if(food == null){
            return;
        }

        holder.itemFoodBinding.tvName.setText(food.getName());

        String strPrice = food.getPrice() + ConstantKey.UNIT_CURRENCY;
        holder.itemFoodBinding.tvPrice.setText(strPrice);
        holder.itemFoodBinding.tvQuantity.setText(String.valueOf(food.getQuantity()));
        holder.itemFoodBinding.imgEdit.setOnClickListener(view -> iManagerFoodListener.editFood(food));
        holder.itemFoodBinding.imgDelete.setOnClickListener(view -> iManagerFoodListener.deleteFood(food));
    }

    @Override
    public int getItemCount() {
        if(foodList != null){
            return foodList.size();
        }

        return 0;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder{
        private final ItemFoodBinding itemFoodBinding;

        public FoodViewHolder(ItemFoodBinding itemFoodBinding){
            super(itemFoodBinding.getRoot());
            this.itemFoodBinding = itemFoodBinding;
        }
    }
}
