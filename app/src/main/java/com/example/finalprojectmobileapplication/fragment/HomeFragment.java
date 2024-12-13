package com.example.finalprojectmobileapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.finalprojectmobileapplication.MyApplication;
import com.example.finalprojectmobileapplication.activity.CategoryActivity;
//import com.example.finalprojectmobileapplication.activity.SearchActivity;
//import com.example.finalprojectmobileapplication.adapter.BannerMovieAdapter;
import com.example.finalprojectmobileapplication.activity.SearchActivity;
import com.example.finalprojectmobileapplication.adapter.CategoryAdapter;
//import com.example.finalprojectmobileapplication.adapter.MovieAdapter;
import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.constant.GlobalFunction;
import com.example.finalprojectmobileapplication.databinding.FragmentHomeBinding;
import com.example.finalprojectmobileapplication.model.Category;
import com.example.finalprojectmobileapplication.model.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final int MAX_BANNER_SIZE = 3;
    private FragmentHomeBinding mFragmentHomeBinding;

    private List<Movie> mListMovie;
    private List<Movie> mListMovieBanner;
    private List<Category> mListCategory;

    private final Handler mHanderBanner = new Handler(Looper.getMainLooper());
    private final Runnable mRunnableBanner = new Runnable() {
        @Override
        public void run() {
            if (mListMovieBanner == null || mListMovieBanner.isEmpty()){
                return;
            }
            if (mFragmentHomeBinding.viewPager2.getCurrentItem() == mListMovieBanner.size() -1){
                mFragmentHomeBinding.viewPager2.setCurrentItem(0);
                return;
            }
            mFragmentHomeBinding.viewPager2.setCurrentItem(mFragmentHomeBinding.viewPager2.getCurrentItem() +1);
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mFragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);

//        getListMovies();
        getListCategory();
        initListener();

        return mFragmentHomeBinding.getRoot();
    }

    private void initListener() {
        mFragmentHomeBinding.layoutSearch.setOnClickListener(view -> GlobalFunction.startActivity(getActivity(), SearchActivity.class));
    }

    private void getListCategory() {
        if (getActivity() == null) {
            return;
        }
        MyApplication.get(getActivity()).getCategoryDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListCategory != null) {
                    mListCategory.clear();
                } else {
                    mListCategory = new ArrayList<>();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    if (category != null) {
                        mListCategory.add(0, category);
                    }
                }
                displayListCategories();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void displayListCategories() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        mFragmentHomeBinding.rcvCategory.setLayoutManager(linearLayoutManager);
        CategoryAdapter categoryAdapter = new  CategoryAdapter(mListCategory, category -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstantKey.KEY_INTENT_CATEGORY_OBJECT, category);
            GlobalFunction.startActivity(getActivity(), CategoryActivity.class, bundle);
        });
        mFragmentHomeBinding.rcvCategory.setAdapter(categoryAdapter);
    }

//    private void getListMovies() {
//        if (getActivity() == null) {
//            return;
//        }
//        MyApplication.get(getActivity().getMovieDatabaseReference().addValueEventListener(new ValueEventListener()))
//    }



}