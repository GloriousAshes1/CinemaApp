package com.example.finalprojectmobileapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.finalprojectmobileapplication.MyApplication;
import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.adapter.MovieAdapter;
import com.example.finalprojectmobileapplication.constant.GlobalFunction;
import com.example.finalprojectmobileapplication.databinding.ActivitySearchBinding;
import com.example.finalprojectmobileapplication.model.Category;
import com.example.finalprojectmobileapplication.model.Movie;
import com.example.finalprojectmobileapplication.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivitySearchBinding mActivitySearchBinding;
    private List<Category> mListCategory;
    private Category mCategorySelected;
    private List<Movie> mListMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySearchBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(mActivitySearchBinding.getRoot());
        
        initListener();
        getListCategory();

    }

    private void initListener() {
        mActivitySearchBinding.imageBack.setOnClickListener(view -> {
            GlobalFunction.hideSoftKeyboard(SearchActivity.this);
            onBackPressed();
        });
        mActivitySearchBinding.imageDelete.setOnClickListener(view -> mActivitySearchBinding.edtKeyword.setText(""));
        mActivitySearchBinding.edtKeyword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMovie();
                return true;
            }
            return false;
        });
    }

    private void getListCategory() {
        MyApplication.get(this).getCategoryDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // This is called whenever the data changes or is first loaded
                mActivitySearchBinding.tvCategoryTitle.setVisibility(View.VISIBLE);
                mActivitySearchBinding.layoutCategory.setVisibility(View.VISIBLE);

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
                mCategorySelected = new Category(0, getString(R.string.label_all), "");
                mListCategory.add(0, mCategorySelected);
                initLayoutCategory("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mActivitySearchBinding.tvCategoryTitle.setVisibility(View.GONE);
                mActivitySearchBinding.layoutCategory.setVisibility(View.GONE);
            }
        });
    }

    private void initLayoutCategory(String tag) {
        mActivitySearchBinding.layoutCategory.removeAllViews();
        if (mListCategory != null && !mListCategory.isEmpty()) {
            for (int i = 0; i < mListCategory.size(); i++) {
                Category category = mListCategory.get(i);

                FlowLayout.LayoutParams params =
                        new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                                FlowLayout.LayoutParams.WRAP_CONTENT);
                TextView textView = new TextView(SearchActivity.this);
                params.setMargins(0, 10, 20, 10);
                textView.setLayoutParams(params);
                textView.setPadding(30, 10, 30, 10);
                textView.setTag(String.valueOf(category.getId()));
                textView.setText(category.getName());

                // show which movies belong to the specific category
                if (tag.equals(String.valueOf(category.getId()))) {
                    mCategorySelected = category;
                    textView.setBackgroundResource(R.drawable.bg_white_shape_round_corner_border_red);
                    textView.setTextColor(getResources().getColor(R.color.red));
                    // show movies
                    searchMovie();
                } else {
                    textView.setBackgroundResource(R.drawable.bg_white_shape_round_corner_border_grey);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                textView.setTextSize(((int) getResources().getDimension(R.dimen.text_size_small) /
                        getResources().getDisplayMetrics().density));
                textView.setOnClickListener(SearchActivity.this);
                mActivitySearchBinding.layoutCategory.addView(textView);
            }
        }
    }

    private boolean isMovieResult(Movie movie) {
        if (movie == null) {
            return false;
        }
        String key = mActivitySearchBinding.edtKeyword.getText().toString().trim();
        long categoryId = mCategorySelected.getId();
        if (StringUtil.isEmpty(key)) {
            if (categoryId == 0) {
                return true;
            } else return movie.getCategoryId() == categoryId;
        } else {
            boolean isMatch = GlobalFunction.getTextSearch(movie.getName()).toLowerCase().trim()
                    .contains(GlobalFunction.getTextSearch(key).toLowerCase().trim());
            if (categoryId == 0) {
                return isMatch;
            } else return isMatch && movie.getCategoryId() == categoryId;
        }
    }


    private void searchMovie() {
        MyApplication.get(this).getMovieDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListMovies != null) {
                    mListMovies.clear();
                } else {
                    mListMovies = new ArrayList<>();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if (isMovieResult(movie)) {
                        mListMovies.add(0, movie);
                    }
                }
                displayListMoviesResult();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void displayListMoviesResult() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mActivitySearchBinding.rcvData.setLayoutManager(gridLayoutManager);
        MovieAdapter movieAdapter = new MovieAdapter(mListMovies,
                movie -> GlobalFunction.goToMovieDetail(this, movie));
        mActivitySearchBinding.rcvData.setAdapter(movieAdapter);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}