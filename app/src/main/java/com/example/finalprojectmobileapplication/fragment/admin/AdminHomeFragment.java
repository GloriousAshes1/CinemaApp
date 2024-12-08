package com.example.finalprojectmobileapplication.fragment.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalprojectmobileapplication.MyApplication;
import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.activity.admin.AddMovieActivity;
import com.example.finalprojectmobileapplication.adapter.admin.AdminMovieAdapter;
import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.constant.GlobalFunction;
import com.example.finalprojectmobileapplication.databinding.FragmentAdminHomeBinding;
import com.example.finalprojectmobileapplication.model.Category;
import com.example.finalprojectmobileapplication.model.Movie;
import com.example.finalprojectmobileapplication.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeFragment extends Fragment implements View.OnClickListener {
    private FragmentAdminHomeBinding fragmentAdminHomeBinding;
    private List<Movie> movieList;
    private AdminMovieAdapter adminMovieAdapter;

    private List<Category> categoryList;
    private Category categorySelected;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAdminHomeBinding = FragmentAdminHomeBinding.inflate(inflater, container, false);

        initListener();
        getListCategory();

        return fragmentAdminHomeBinding.getRoot();
    }

    private void initListener(){
        fragmentAdminHomeBinding.btnAddMovie.setOnClickListener(view -> onClickAddMovie());

        fragmentAdminHomeBinding.imgSearch.setOnClickListener(view -> searchMovie());

        fragmentAdminHomeBinding.edtSearchName.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_SEARCH){
                searchMovie();
                return true;
            }
            return false;
        });

        fragmentAdminHomeBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if(strKey.equals("") || strKey.length() == 0){
                    searchMovie();
                }
            }
        });
    }

    private void getListCategory(){
        if(getActivity() == null){
            return;
        }

        MyApplication.get(getActivity()).getCategoryDatabaseReference().addValueEventListener(new ValueEventListener() {
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
                    if(category != null){
                        categoryList.add(0, category);
                    }
                }

                categorySelected = new Category(0, getString(R.string.label_all), "");
                categoryList.add(categorySelected);
                initLayoutCategory("0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void initLayoutCategory(String tag){
        fragmentAdminHomeBinding.layoutCategory.removeAllViews();

        if(categoryList != null && !categoryList.isEmpty()){
            for(int i = 0; i < categoryList.size(); i++){
                Category category = categoryList.get(i);

                FlowLayout.LayoutParams params =
                        new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                FlowLayout.LayoutParams.WRAP_CONTENT);

                TextView textView = new TextView(getActivity());
                params.setMargins(0, 10, 20, 10);
                textView.setLayoutParams(params);
                textView.setPadding(30, 10, 30, 10);
                textView.setTag(String.valueOf(category.getId()));
                textView.setText(category.getName());

                if(tag.equals(String.valueOf(category.getId()))){
                    categorySelected = category;
                    textView.setBackgroundResource(R.drawable.bg_white_shape_round_corner_border_red);
                    textView.setTextColor(getResources().getColor(R.color.red));
                    searchMovie();
                }
                else{
                    textView.setBackgroundResource(R.drawable.bg_white_shape_round_corner_border_grey);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

                textView.setTextSize((int) getResources().getDimension(R.dimen.text_size_xsmall) /
                        getResources().getDisplayMetrics().density);
                textView.setOnClickListener(this);
                fragmentAdminHomeBinding.layoutCategory.addView(textView);
            }
        }
    }

    private void onClickAddMovie(){
        GlobalFunction.startActivity(getActivity(), AddMovieActivity.class);
    }

    private void onClickEditMovie(Movie movie){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantKey.KEY_INTENT_MOVIE_OBJECT, movie);
        GlobalFunction.startActivity(getActivity(), AddMovieActivity.class, bundle);
    }

    private void deleteMovieItem(Movie movie){
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if(getActivity() == null){
                        return;
                    }

                    MyApplication.get(getActivity()).getMovieDatabaseReference()
                            .child(String.valueOf(movie.getId())).removeValue((error, ref) -> {
                                Toast.makeText(getActivity(), getString(R.string.msg_delete_movie_successfully), Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    public void searchMovie(){
        if(getActivity() == null){
            return;
        }

        GlobalFunction.hideSoftKeyboard(getActivity());

        MyApplication.get(getActivity()).getMovieDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(movieList != null){
                    movieList.clear();
                }
                else{
                    movieList = new ArrayList<>();
                }

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if(isMovieResult(movie)){
                        movieList.add(0, movie);
                    }
                }

                loadListMovie();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadListMovie(){
        if(getActivity() == null){
            return;
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentAdminHomeBinding.rcvMovie.setLayoutManager(linearLayoutManager);

        adminMovieAdapter = new AdminMovieAdapter(getActivity(), movieList, new AdminMovieAdapter.IManagerMovieListener() {
            @Override
            public void editMovie(Movie movie) {
                onClickEditMovie(movie);
            }

            @Override
            public void deleteMovie(Movie movie) {
                deleteMovieItem(movie);
            }

            @Override
            public void clickItemMovie(Movie movie) {}
        });

        fragmentAdminHomeBinding.rcvMovie.setAdapter(adminMovieAdapter);
    }

    private boolean isMovieResult(Movie movie){
        if(movie == null){
            return false;
        }

        String key = fragmentAdminHomeBinding.edtSearchName.getText().toString().trim();
        long categoryId = 0;

        if(categorySelected != null){
            categoryId = categorySelected.getId();
        }

        if(StringUtil.isEmpty(key)){
            if(categoryId == 0){
                return true;
            }
            else{
                return movie.getCategoryId() == categoryId;
            }
        }

        else{
            boolean isMatch = GlobalFunction.getTextSearch(movie.getName()).toLowerCase().trim()
                    .contains(GlobalFunction.getTextSearch(key).toLowerCase().trim());

            if(categoryId == 0){
                return isMatch;
            }
            else{
                return isMatch && movie.getCategoryId() == categoryId;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(adminMovieAdapter != null){
            adminMovieAdapter.release();
        }
    }

    @Override
    public void onClick(View view) {
        initLayoutCategory(view.getTag().toString());
    }
}