package com.example.finalprojectmobileapplication.adapter.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.finalprojectmobileapplication.fragment.admin.AdminBookingFragment;
import com.example.finalprojectmobileapplication.fragment.admin.AdminCategoryFragment;
import com.example.finalprojectmobileapplication.fragment.admin.AdminFoodFragment;
import com.example.finalprojectmobileapplication.fragment.admin.AdminHomeFragment;
import com.example.finalprojectmobileapplication.fragment.admin.AdminManageFragment;


public class AdminViewPagerAdapter extends FragmentStateAdapter {
    public AdminViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new AdminFoodFragment();
            case 2:
                return new AdminHomeFragment();
            case 3:
                return new AdminBookingFragment();
            case 4:
                return new AdminManageFragment();
            default:
                return new AdminCategoryFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
