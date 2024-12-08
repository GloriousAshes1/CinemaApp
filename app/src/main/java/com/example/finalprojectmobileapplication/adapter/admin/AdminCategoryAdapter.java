package com.example.finalprojectmobileapplication.adapter.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectmobileapplication.databinding.ItemCategoryAdminBinding;
import com.example.finalprojectmobileapplication.model.Category;
import com.example.finalprojectmobileapplication.util.GlideUtils;

import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.CategoryViewHolder> {
    private final List<Category> categoryList;
    private final IManagerCategoryListener iManagerCategoryListener;

    public interface IManagerCategoryListener{
        void editCategory(Category category);

        void deleteCategory(Category category);
    }

    public AdminCategoryAdapter(List<Category>categoryList, IManagerCategoryListener iManagerCategoryListener){
        this.categoryList = categoryList;
        this.iManagerCategoryListener =iManagerCategoryListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryAdminBinding itemCategoryAdminBinding = ItemCategoryAdminBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(itemCategoryAdminBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);

        if(category == null){
            return;
        }

        GlideUtils.loadUrl(category.getImage(), holder.itemCategoryAdminBinding.imgCategory);
        holder.itemCategoryAdminBinding.tvCategoryName.setText(category.getName());
        holder.itemCategoryAdminBinding.imgEdit.setOnClickListener(view -> iManagerCategoryListener.editCategory(category));
        holder.itemCategoryAdminBinding.imgDelete.setOnClickListener(view -> iManagerCategoryListener.deleteCategory(category));
    }

    @Override
    public int getItemCount() {
        if(categoryList != null){
            return categoryList.size();
        }
        return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        private final ItemCategoryAdminBinding itemCategoryAdminBinding;

        public CategoryViewHolder(@NonNull ItemCategoryAdminBinding itemCategoryAdminBinding){
            super(itemCategoryAdminBinding.getRoot());
            this.itemCategoryAdminBinding = itemCategoryAdminBinding;
        }
    }

}
