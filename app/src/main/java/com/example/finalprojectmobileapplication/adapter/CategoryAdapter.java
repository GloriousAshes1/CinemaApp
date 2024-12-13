package com.example.finalprojectmobileapplication.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectmobileapplication.adapter.admin.AdminCategoryAdapter;
import com.example.finalprojectmobileapplication.databinding.ItemCategoryBinding;
import com.example.finalprojectmobileapplication.model.Category;
import com.example.finalprojectmobileapplication.util.GlideUtils;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private final List<Category> mListCategory;
    private final IManagerCategoryListener iManagerCategoryListener;

    public interface IManagerCategoryListener {
        void clickItemCategory(Category category);
    }

    public CategoryAdapter(List<Category> mListCategory, IManagerCategoryListener iManagerCategoryListener) {
        this.mListCategory = mListCategory;
        this.iManagerCategoryListener = iManagerCategoryListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding mItemCategoryBinding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(mItemCategoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mListCategory.get(position);
        if (category == null){
            return;
        }
        GlideUtils.loadUrl(category.getImage(), holder.mItemCategoryBinding.imgCategory);
        holder.mItemCategoryBinding.tvCategoryName.setText(category.getName());
        holder.mItemCategoryBinding.layoutItem.setOnClickListener(view -> iManagerCategoryListener.clickItemCategory(category));

    }

    @Override
    public int getItemCount() {
        if (mListCategory != null){
            return mListCategory.size();
        }
        return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final ItemCategoryBinding mItemCategoryBinding;

        public CategoryViewHolder(@NonNull ItemCategoryBinding itemCategoryBinding){
            super(itemCategoryBinding.getRoot());
            this.mItemCategoryBinding = itemCategoryBinding;
        }
    }
}
