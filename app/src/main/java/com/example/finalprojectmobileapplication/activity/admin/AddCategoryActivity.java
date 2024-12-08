package com.example.finalprojectmobileapplication.activity.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finalprojectmobileapplication.MyApplication;
import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.activity.BaseActivity;
import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.constant.GlobalFunction;
import com.example.finalprojectmobileapplication.databinding.ActivityAddCategoryBinding;
import com.example.finalprojectmobileapplication.model.Category;
import com.example.finalprojectmobileapplication.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddCategoryActivity extends BaseActivity {

    private ActivityAddCategoryBinding activityAddCategoryBinding;
    private boolean isUpdate;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddCategoryBinding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(activityAddCategoryBinding.getRoot());

        Bundle bundleReceived = getIntent().getExtras();

        if(bundleReceived != null){
            isUpdate = true;
            category = (Category) bundleReceived.get(ConstantKey.KEY_INTENT_CATEGORY_OBJECT);
        }

        initView();

        activityAddCategoryBinding.imgBack.setOnClickListener(view -> onBackPressed());
        activityAddCategoryBinding.btnAddOrEdit.setOnClickListener(view -> addOrEditCategory());
    }

    private void initView(){
        if(isUpdate){
            activityAddCategoryBinding.tvTitle.setText(getString(R.string.edit_category_title));
            activityAddCategoryBinding.btnAddOrEdit.setText(getString(R.string.edit_category_title));
            activityAddCategoryBinding.edtName.setText(category.getName());
            activityAddCategoryBinding.edtImage.setText(category.getImage());
        }
        else{
            activityAddCategoryBinding.tvTitle.setText(getString(R.string.add_category_title));
            activityAddCategoryBinding.btnAddOrEdit.setText(getString(R.string.add_category_title));
        }
    }


    private void addOrEditCategory(){
        String strName = activityAddCategoryBinding.edtName.getText().toString().trim();
        String strImage = activityAddCategoryBinding.edtImage.getText().toString().trim();

        if(StringUtil.isEmpty(strName)){
            Toast.makeText(this, getString(R.string.msg_name_category_require), Toast.LENGTH_SHORT).show();
        }

        else if(StringUtil.isEmpty(strImage)){
            Toast.makeText(this, getString(R.string.msg_image_category_require), Toast.LENGTH_SHORT).show();
        }

        else{
            DatabaseReference categoryRef = MyApplication.get(this).getCategoryDatabaseReference();

            // Update category
            if(isUpdate){
                showProgressDialog(true);
                Map<String, Object> map = new HashMap<>();
                map.put("name", strName);
                map.put("image", strImage);

                categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean duplicateFound = false;
                        String existName = "";

                        showProgressDialog(false);

                        //Check category's name duplicate
                        for(DataSnapshot categorySnapshot : snapshot.getChildren()){
                            existName = categorySnapshot.child("name").getValue(String.class);

                            if(existName != null && existName.equals(strName)){
                                Toast.makeText(AddCategoryActivity.this, getString(R.string.msg_duplicate_category_name), Toast.LENGTH_SHORT).show();
                                duplicateFound = true;
                                break;
                            }
                        }

                        // Not duplicate
                        if(!duplicateFound){
                            categoryRef
                                    .child(String.valueOf(category.getId())).updateChildren(map, (error, ref) -> {
                                        Toast.makeText(AddCategoryActivity.this, getString(R.string.msg_edit_category_successfully), Toast.LENGTH_SHORT).show();
                                        GlobalFunction.hideSoftKeyboard(AddCategoryActivity.this);
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                MyApplication.get(this).getCategoryDatabaseReference()
//                        .child(String.valueOf(category.getId())).updateChildren(map, (error, ref) -> {
//                            showProgressDialog(false);
//                            Toast.makeText(this, getString(R.string.msg_edit_category_successfully), Toast.LENGTH_SHORT).show();
//                            GlobalFunction.hideSoftKeyboard(AddCategoryActivity.this);
//                        });
            }
            // Add new category
            else{
                showProgressDialog(true);
                //Category ID = time
                long categoryId = System.currentTimeMillis();

                Category newCategory = new Category(categoryId, strName, strImage);

                System.out.println("Add new category here 3 !!!");

                categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean duplicateFound = false;
                        String existName = "";

                        System.out.println("Add new category here 1 !!!");

                        showProgressDialog(false);

                        //Check category's name duplicate
                        for(DataSnapshot categorySnapshot : snapshot.getChildren()){
                            existName = categorySnapshot.child("name").getValue(String.class);

                            if(existName != null && existName.equals(strName)){
                                Toast.makeText(AddCategoryActivity.this, getString(R.string.msg_duplicate_category_name), Toast.LENGTH_SHORT).show();
                                duplicateFound = true;
                                break;
                            }
                        }

                        System.out.println("Add new category here 2 !!!");

                        // Not duplicate
                        if(!duplicateFound){
                            categoryRef.child(String.valueOf(categoryId)).setValue(newCategory, (error, ref) -> {
                                activityAddCategoryBinding.edtName.setText("");
                                activityAddCategoryBinding.edtImage.setText("");
                                GlobalFunction.hideSoftKeyboard(AddCategoryActivity.this);
                                Toast.makeText(AddCategoryActivity.this, getString(R.string.msg_add_category_successfully), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//                MyApplication.get(this).getCategoryDatabaseReference().child(String.valueOf(categoryId)).setValue(newCategory, (error, ref) -> {
//                   showProgressDialog(false);
//                   activityAddCategoryBinding.edtName.setText("");
//                   activityAddCategoryBinding.edtImage.setText("");
//                   GlobalFunction.hideSoftKeyboard(this);
//                    Toast.makeText(this, getString(R.string.msg_add_category_successfully), Toast.LENGTH_SHORT).show();
//                });
            }
        }
    }
}