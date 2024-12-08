package com.example.finalprojectmobileapplication.activity.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finalprojectmobileapplication.MyApplication;
import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.activity.BaseActivity;
import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.constant.GlobalFunction;
import com.example.finalprojectmobileapplication.databinding.ActivityAddFoodBinding;
import com.example.finalprojectmobileapplication.model.Food;
import com.example.finalprojectmobileapplication.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddFoodActivity extends BaseActivity {

    private ActivityAddFoodBinding activityAddFoodBinding;
    private boolean isUpdate;
    private Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddFoodBinding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        setContentView(activityAddFoodBinding.getRoot());

        Bundle bundleReceived = getIntent().getExtras();
        if(bundleReceived != null){
           isUpdate = true;
           food = (Food) bundleReceived.get(ConstantKey.KEY_INTENT_FOOD_OBJECT);
        }

        initView();

        activityAddFoodBinding.imgBack.setOnClickListener(view -> onBackPressed());
        activityAddFoodBinding.btnAddOrEdit.setOnClickListener(view -> addOrEditFood());
    }

    private void initView(){
        if(isUpdate){
            activityAddFoodBinding.tvTitle.setText(getString(R.string.edit_food_title));
            activityAddFoodBinding.btnAddOrEdit.setText(getString(R.string.action_edit));
            activityAddFoodBinding.edtName.setText(food.getName());
            activityAddFoodBinding.edtPrice.setText(String.valueOf(food.getPrice()));
            activityAddFoodBinding.edtQuantity.setText(String.valueOf(food.getQuantity()));
        }
        else{
            activityAddFoodBinding.tvTitle.setText(getString(R.string.add_food_title));
            activityAddFoodBinding.btnAddOrEdit.setText(getString(R.string.action_add));
        }
    }

    private void addOrEditFood(){
        String strName = activityAddFoodBinding.edtName.getText().toString().trim();
        String strPrice = activityAddFoodBinding.edtPrice.getText().toString().trim();
        String strQuantity = activityAddFoodBinding.edtQuantity.getText().toString().trim();

        if(StringUtil.isEmpty(strName)){
            Toast.makeText(this, getString(R.string.msg_name_food_require), Toast.LENGTH_SHORT).show();
        }
        else if(StringUtil.isEmpty(strPrice)){
            Toast.makeText(this, getString(R.string.msg_price_food_require), Toast.LENGTH_SHORT).show();
        }
        else if(Integer.parseInt(strPrice) < 20){
            Toast.makeText(this, getString(R.string.minimum_price), Toast.LENGTH_SHORT).show();
        }
        else if(StringUtil.isEmpty(strQuantity)){
            Toast.makeText(this, getString(R.string.msg_quantity_food_require), Toast.LENGTH_SHORT).show();
        }
        else if(Integer.parseInt(strQuantity) == 0){
            Toast.makeText(this, getString(R.string.minimum_quantity), Toast.LENGTH_SHORT).show();
        }
        else{
            DatabaseReference foodRef = MyApplication.get(this).getFoodDatabaseReference();

            if(isUpdate){
                showProgressDialog(true);
                Map<String, Object> map = new HashMap<>();
                map.put("name", strName);
                map.put("price", Integer.parseInt(strPrice));
                map.put("quantity", Integer.parseInt(strQuantity));


                foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean duplicateFound = false;
                        String existFoodName = "";
                        long existFoodId = 0;
                        showProgressDialog(false);

                        for(DataSnapshot foodSnapshot : snapshot.getChildren()){
                            existFoodName = foodSnapshot.child("name").getValue(String.class);
                            existFoodId = foodSnapshot.child("id").getValue(Long.class);

                            if(existFoodName != null && existFoodName.equals(strName) && existFoodId != food.getId()){
                                duplicateFound = true;
                                Toast.makeText(AddFoodActivity.this, getString(R.string.msg_duplicate_food_name), Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                        if(!duplicateFound){
                            foodRef.child(String.valueOf(food.getId())).updateChildren(map, (error, ref) -> {
                                        showProgressDialog(false);
                                        Toast.makeText(AddFoodActivity.this, getString(R.string.msg_edit_food_successfully), Toast.LENGTH_SHORT).show();
                                        GlobalFunction.hideSoftKeyboard(AddFoodActivity.this);
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                MyApplication.get(this).getFoodDatabaseReference()
//                        .child(String.valueOf(food.getId())).updateChildren(map, (error, ref) -> {
//                            showProgressDialog(false);
//                            Toast.makeText(this, getString(R.string.msg_edit_food_successfully), Toast.LENGTH_SHORT).show();
//                            GlobalFunction.hideSoftKeyboard(AddFoodActivity.this);
//                        });
            }
            else{
                showProgressDialog(true);
                long foodId = System.currentTimeMillis();

                Food newFood = new Food(foodId, strName, Integer.parseInt(strPrice), Integer.parseInt(strQuantity));

                foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean duplicateFound = false;
                        String existFoodName = "";
                        showProgressDialog(false);

                        for(DataSnapshot foodSnapshot : snapshot.getChildren()){
                            existFoodName = foodSnapshot.child("name").getValue(String.class);

                            if(existFoodName != null && existFoodName.equals(strName)){
                                duplicateFound = true;
                                Toast.makeText(AddFoodActivity.this, getString(R.string.msg_duplicate_food_name), Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                        if(!duplicateFound){
                            foodRef.child(String.valueOf(foodId)).setValue(newFood, (error, ref) -> {
                                showProgressDialog(false);
                                activityAddFoodBinding.edtName.setText("");
                                activityAddFoodBinding.edtPrice.setText("");
                                activityAddFoodBinding.edtQuantity.setText("");
                                GlobalFunction.hideSoftKeyboard(AddFoodActivity.this);
                                Toast.makeText(AddFoodActivity.this, getString(R.string.msg_add_food_successfully), Toast.LENGTH_SHORT).show();
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//                MyApplication.get(this).getFoodDatabaseReference().child(String.valueOf(foodId)).setValue(newFood, (error, ref) -> {
//                    showProgressDialog(false);
//                    activityAddFoodBinding.edtName.setText("");
//                    activityAddFoodBinding.edtPrice.setText("");
//                    activityAddFoodBinding.edtQuantity.setText("");
//                    GlobalFunction.hideSoftKeyboard(this);
//                    Toast.makeText(this, getString(R.string.msg_add_food_successfully), Toast.LENGTH_SHORT).show();
//                });
            }
        }
    }
}