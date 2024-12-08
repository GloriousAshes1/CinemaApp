package com.example.finalprojectmobileapplication.fragment.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalprojectmobileapplication.MyApplication;
import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.activity.admin.AddCategoryActivity;
import com.example.finalprojectmobileapplication.adapter.admin.AdminCategoryAdapter;
import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.constant.GlobalFunction;
import com.example.finalprojectmobileapplication.databinding.FragmentAdminCategoryBinding;
import com.example.finalprojectmobileapplication.model.Category;
import com.example.finalprojectmobileapplication.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminCategoryFragment extends Fragment {
    private FragmentAdminCategoryBinding fragmentAdminCategoryBinding;
    private List<Category> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentAdminCategoryBinding = FragmentAdminCategoryBinding.inflate(inflater, container, false);

        initListener();
        getListCategory("");

        return fragmentAdminCategoryBinding.getRoot();
    }

    private void initListener(){
        fragmentAdminCategoryBinding.btnAddCategory.setOnClickListener(view -> onClickAddCategory());

        fragmentAdminCategoryBinding.imgSearch.setOnClickListener(view -> searchCategory());

        fragmentAdminCategoryBinding.edtSearchName.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_SEARCH){
                searchCategory();
                return true;
            }
            return false;
        });

        fragmentAdminCategoryBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strKey = editable.toString().trim();
                if(StringUtil.isEmpty(strKey) || strKey.length() == 0){
                    getListCategory("");
                }
            }
        });
    }

    private void searchCategory(){
        String strKey = fragmentAdminCategoryBinding.edtSearchName.getText().toString().trim();
        getListCategory(strKey);
        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    private void onClickAddCategory(){
        GlobalFunction.startActivity(getActivity(), AddCategoryActivity.class);
    }

    private void onClickEditCategory(Category category){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantKey.KEY_INTENT_CATEGORY_OBJECT, category);
        GlobalFunction.startActivity(getActivity(), AddCategoryActivity.class, bundle);
    }

    private void deleteCategoryItem(Category category){
        DatabaseReference movieRef = MyApplication.get(getActivity()).getMovieDatabaseReference();
        DatabaseReference categoryRef = MyApplication.get(getActivity()).getCategoryDatabaseReference();

        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if(getActivity() == null){
                        return;
                    }

                    movieRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean categoryWithMovie = false;
                            long categoryId = 0;

                            for(DataSnapshot movieSnapshot : snapshot.getChildren()){
                                categoryId = movieSnapshot.child("categoryId").getValue(Long.class);

                                if(categoryId == category.getId()){
                                    categoryWithMovie = true;
                                    Toast.makeText(getActivity(), getString(R.string.category_with_movie), Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            if(!categoryWithMovie){
                                categoryRef.child(String.valueOf(category.getId())).removeValue((error, ref) -> {
                                    Toast.makeText(getActivity(), getString(R.string.msg_delete_category_successfully), Toast.LENGTH_SHORT).show();
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


//                    categoryRef.child(String.valueOf(category.getId())).removeValue((error, ref) -> {
//                                Toast.makeText(getActivity(), getString(R.string.msg_delete_category_successfully), Toast.LENGTH_SHORT).show();
//                            });
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void getListCategory(String key){
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
                        if(StringUtil.isEmpty(key)){
                            categoryList.add(0, category);
                        }
                        else{
                            if(GlobalFunction.getTextSearch(category.getName()).toLowerCase().trim()
                                    .contains(GlobalFunction.getTextSearch(key).toLowerCase().trim())){
                                categoryList.add(0, category);
                            }
                        }
                    }
                }
                loadListData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadListData(){
        if(getActivity() == null){
            return;
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentAdminCategoryBinding.rcvCategory.setLayoutManager(linearLayoutManager);

        AdminCategoryAdapter adminCategoryAdapter = new AdminCategoryAdapter(categoryList,
                new AdminCategoryAdapter.IManagerCategoryListener() {
                    @Override
                    public void editCategory(Category category) {
                        onClickEditCategory(category);
                    }

                    @Override
                    public void deleteCategory(Category category) {
                        deleteCategoryItem(category);
                    }
                });
        fragmentAdminCategoryBinding.rcvCategory.setAdapter(adminCategoryAdapter);
    }
}