package com.example.finalprojectmobileapplication.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finalprojectmobileapplication.MyApplication;
import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.activity.BaseActivity;
import com.example.finalprojectmobileapplication.adapter.admin.AdminSelectCategoryAdapter;
import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.constant.GlobalFunction;
import com.example.finalprojectmobileapplication.databinding.ActivityAddMovieBinding;
import com.example.finalprojectmobileapplication.model.Category;
import com.example.finalprojectmobileapplication.model.Movie;
import com.example.finalprojectmobileapplication.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMovieActivity extends BaseActivity {

    private ActivityAddMovieBinding activityAddMovieBinding;
    private boolean isUpdate;
    private Movie movie;
    private List<Category> categoryList;
    private Category categorySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddMovieBinding = ActivityAddMovieBinding.inflate(getLayoutInflater());
        setContentView(activityAddMovieBinding.getRoot());

        Bundle bundleReceived = getIntent().getExtras();
        if(bundleReceived != null){
            isUpdate = true;
            movie = (Movie) bundleReceived.get(ConstantKey.KEY_INTENT_MOVIE_OBJECT);
        }

        initView();
        getListCategory();

        activityAddMovieBinding.imgBack.setOnClickListener(view -> onBackPressed());
        activityAddMovieBinding.btnAddOrEdit.setOnClickListener(view -> addOrEditMovie());
        activityAddMovieBinding.tvDate.setOnClickListener(view -> {
            if(isUpdate){
                GlobalFunction.showDatePicker(AddMovieActivity.this, movie.getDate(), date -> activityAddMovieBinding.tvDate.setText(date));
            }
            else{
                GlobalFunction.showDatePicker(AddMovieActivity.this, "", date -> activityAddMovieBinding.tvDate.setText(date));
            }
        });
    }

    private void initView(){
        if(isUpdate){
            activityAddMovieBinding.tvTitle.setText(getString(R.string.edit_movie_title));
            activityAddMovieBinding.btnAddOrEdit.setText(getString(R.string.action_edit));

            activityAddMovieBinding.edtName.setText(movie.getName());
            activityAddMovieBinding.edtDescription.setText(movie.getDescription());
            activityAddMovieBinding.edtPrice.setText(String.valueOf(movie.getPrice()));
            activityAddMovieBinding.tvDate.setText(movie.getDate());
            activityAddMovieBinding.edtImage.setText(movie.getImage());
            activityAddMovieBinding.edtImageBanner.setText(movie.getImageBanner());
            activityAddMovieBinding.edtVideo.setText(movie.getUrl());
        }
        else{
            activityAddMovieBinding.tvTitle.setText(getString(R.string.add_movie_title));
            activityAddMovieBinding.btnAddOrEdit.setText(getString(R.string.action_add));
        }
    }

    private void getListCategory(){
        MyApplication.get(this).getCategoryDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(categoryList != null){
                    categoryList.clear();
                }
                else{
                    categoryList = new ArrayList<>();
                }

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Category category = dataSnapshot.getValue(Category.class);
                    categoryList.add(0, category);
                }
                initSpinnerCategory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private int getPositionCategoryUpdate(Movie movie){
        if(categoryList == null || categoryList.isEmpty()){
            return 0;
        }

        for(int i = 0; i < categoryList.size(); i++){
            if(movie.getCategoryId() == categoryList.get(i).getId()){
                return i;
            }
        }
        return 0;
    }

    private void initSpinnerCategory(){
         AdminSelectCategoryAdapter selectCategoryAdapter = new AdminSelectCategoryAdapter(this,
                R.layout.item_choose_option, categoryList);
        activityAddMovieBinding.spnCategory.setAdapter(selectCategoryAdapter);
        activityAddMovieBinding.spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categorySelected = selectCategoryAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        if(isUpdate){
            activityAddMovieBinding.spnCategory.setSelection(getPositionCategoryUpdate(movie));
        }
    }

    private void addOrEditMovie(){
        String strName = activityAddMovieBinding.edtName.getText().toString().trim();
        String strDescription = activityAddMovieBinding.edtDescription.getText().toString().trim();
        String strPrice = activityAddMovieBinding.edtPrice.getText().toString().trim();
        String strDate = activityAddMovieBinding.tvDate.getText().toString().trim();
        String strImage = activityAddMovieBinding.edtImage.getText().toString().trim();
        String strImageBanner = activityAddMovieBinding.edtImageBanner.getText().toString().trim();
        String strVideo = activityAddMovieBinding.edtVideo.getText().toString().trim();

        if(categorySelected == null || categorySelected.getId() < 0){
            Toast.makeText(this, getString(R.string.msg_category_movie_require), Toast.LENGTH_SHORT).show();
        }

        else if(StringUtil.isEmpty(strName)){
            Toast.makeText(this, getString(R.string.msg_name_movie_require), Toast.LENGTH_SHORT).show();
        }

        else if (StringUtil.isEmpty(strDescription)) {
            Toast.makeText(this, getString(R.string.msg_description_movie_require), Toast.LENGTH_SHORT).show();
        }

        else if (StringUtil.isEmpty(strPrice)) {
            Toast.makeText(this, getString(R.string.msg_price_movie_require), Toast.LENGTH_SHORT).show();
        }

        else if(Integer.parseInt(strPrice) == 0){
            Toast.makeText(this, getString(R.string.minimum_price), Toast.LENGTH_SHORT).show();
        }

        else if (StringUtil.isEmpty(strDate)) {
            Toast.makeText(this, getString(R.string.msg_date_movie_require), Toast.LENGTH_SHORT).show();
        }

        else if (StringUtil.isEmpty(strImage)) {
            Toast.makeText(this, getString(R.string.msg_image_movie_require), Toast.LENGTH_SHORT).show();
        }

        else if (StringUtil.isEmpty(strImageBanner)) {
            Toast.makeText(this, getString(R.string.msg_image_banner_movie_require), Toast.LENGTH_SHORT).show();
        }

        else if (StringUtil.isEmpty(strVideo)) {
            Toast.makeText(this, getString(R.string.msg_video_movie_require), Toast.LENGTH_SHORT).show();
        }

        else {
            DatabaseReference movieRef = MyApplication.get(this).getMovieDatabaseReference();

            if(isUpdate){
                showProgressDialog(true);
                Map<String, Object> map = new HashMap<>();
                map.put("name", strName);
                map.put("description", strDescription);
                map.put("price", Integer.parseInt(strPrice));

                if(!strDate.equals(movie.getDate())){
                    map.put("date", strDate);
                    map.put("rooms", GlobalFunction.getListRooms());
                }
                map.put("image", strImage);
                map.put("imageBanner", strImageBanner);
                map.put("url", strVideo);
                map.put("categoryId", categorySelected.getId());
                map.put("categoryName", categorySelected.getName());

                movieRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean duplicateFound = false;
                        String existMovieName = "";
                        long existMovieId = 0;
                        showProgressDialog(false);

                        for(DataSnapshot movieSnapshot : snapshot.getChildren()){
                            existMovieName = movieSnapshot.child("name").getValue(String.class);
                            existMovieId = movieSnapshot.child("id").getValue(Long.class);

                            if(existMovieName != null && existMovieName.equals(strName) && existMovieId != movie.getId()){
                                duplicateFound = true;
                                Toast.makeText(AddMovieActivity.this, getString(R.string.msg_duplicate_movie_name), Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                        if(!duplicateFound){
                            movieRef.child(String.valueOf(movie.getId())).updateChildren(map, (error, ref) -> {
                                        showProgressDialog(false);
                                        Toast.makeText(AddMovieActivity.this, getString(R.string.msg_edit_movie_successfully), Toast.LENGTH_SHORT).show();
                                        GlobalFunction.hideSoftKeyboard(AddMovieActivity.this);
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//                MyApplication.get(this).getMovieDatabaseReference()
//                        .child(String.valueOf(movie.getId())).updateChildren(map, (error, ref) -> {
//                            showProgressDialog(false);
//                            Toast.makeText(AddMovieActivity.this, getString(R.string.msg_add_movie_successfully), Toast.LENGTH_SHORT).show();
//                            GlobalFunction.hideSoftKeyboard(this);
//                        });
            }

            else{
                showProgressDialog(true);
                long movieId = System.currentTimeMillis();
                Movie newMovie = new Movie(movieId, strName, strDescription, Integer.parseInt(strPrice),
                        strDate, strImage, strImageBanner, strVideo, GlobalFunction.getListRooms(),
                        categorySelected.getId(), categorySelected.getName(), 0);

                movieRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean duplicateFound = false;
                        String existMovieName = "";
                        showProgressDialog(false);

                        for(DataSnapshot movieSnapshot : snapshot.getChildren()){
                            existMovieName = movieSnapshot.child("name").getValue(String.class);

                            if(existMovieName != null && existMovieName.equals(strName)){
                                duplicateFound = true;
                                Toast.makeText(AddMovieActivity.this, getString(R.string.msg_duplicate_movie_name), Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                        if(!duplicateFound){
                           movieRef.child(String.valueOf(movieId)).setValue(newMovie, (error, ref) -> {
                                showProgressDialog(false);
                                activityAddMovieBinding.spnCategory.setSelection(0);
                                activityAddMovieBinding.edtName.setText("");
                                activityAddMovieBinding.edtDescription.setText("");
                                activityAddMovieBinding.edtPrice.setText("");
                                activityAddMovieBinding.tvDate.setText("");
                                activityAddMovieBinding.edtImage.setText("");
                                activityAddMovieBinding.edtImageBanner.setText("");
                                activityAddMovieBinding.edtVideo.setText("");

                                GlobalFunction.hideSoftKeyboard(AddMovieActivity.this);
                                Toast.makeText(AddMovieActivity.this, getString(R.string.msg_add_movie_successfully), Toast.LENGTH_SHORT).show();
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//                MyApplication.get(this).getMovieDatabaseReference().child(String.valueOf(movieId)).setValue(newMovie, (error, ref) -> {
//                   showProgressDialog(false);
//                   activityAddMovieBinding.spnCategory.setSelection(0);
//                   activityAddMovieBinding.edtName.setText("");
//                   activityAddMovieBinding.edtDescription.setText("");
//                   activityAddMovieBinding.edtPrice.setText("");
//                   activityAddMovieBinding.tvDate.setText("");
//                   activityAddMovieBinding.edtImage.setText("");
//                   activityAddMovieBinding.edtImageBanner.setText("");
//                   activityAddMovieBinding.edtVideo.setText("");
//
//                   GlobalFunction.hideSoftKeyboard(this);
//                    Toast.makeText(this, getString(R.string.msg_add_movie_successfully), Toast.LENGTH_SHORT).show();
//                });
            }
        }
    }
}