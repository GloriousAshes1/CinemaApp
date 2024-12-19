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
import com.example.finalprojectmobileapplication.activity.admin.AddFoodActivity;
import com.example.finalprojectmobileapplication.adapter.admin.AdminFoodAdapter;
import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.constant.GlobalFunction;
import com.example.finalprojectmobileapplication.databinding.FragmentAdminFoodBinding;
import com.example.finalprojectmobileapplication.model.Food;
import com.example.finalprojectmobileapplication.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminFoodFragment extends Fragment {
    private FragmentAdminFoodBinding fragmentAdminFoodBinding;
    private List<Food> foodList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentAdminFoodBinding = FragmentAdminFoodBinding.inflate(inflater, container, false);

        getListFoods("");
        initListener();

        return fragmentAdminFoodBinding.getRoot();
    }

    private void initListener(){
        fragmentAdminFoodBinding.btnAddFood.setOnClickListener(view -> onClickAddFood());

        fragmentAdminFoodBinding.imgSearch.setOnClickListener(view -> searchFood());

        fragmentAdminFoodBinding.edtSearchName.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_SEARCH){
                searchFood();
                return true;
            }
            return false;
        });

        fragmentAdminFoodBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
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
                    getListFoods("");
                }
            }
        });
    }

    private void searchFood(){
        String strKey = fragmentAdminFoodBinding.edtSearchName.getText().toString().trim();
        getListFoods(strKey);
        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    private void onClickAddFood(){
        GlobalFunction.startActivity(getActivity(), AddFoodActivity.class);
    }

    private void onClickEditFood(Food food){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantKey.KEY_INTENT_FOOD_OBJECT, food);
        GlobalFunction.startActivity(getActivity(), AddFoodActivity.class, bundle);
    }

    private void deleteFoodItem(Food food){
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if(getActivity() == null){
                        return;
                    }

                    MyApplication.get(getActivity()).getBookingDatabaseReference().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String foods = "";
                            boolean isBooked = false;
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                foods = dataSnapshot.child("foods").getValue(String.class);
                                if(foods.contains(food.getName())){
                                    isBooked = true;
                                    Toast.makeText(getActivity(), getString(R.string.food_booked), Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            if(!isBooked){
                                MyApplication.get(getActivity()).getFoodDatabaseReference()
                                        .child(String.valueOf(food.getId())).removeValue((error, ref) -> {
                                            Toast.makeText(getActivity(), getString(R.string.msg_delete_food_successfully), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


//                    MyApplication.get(getActivity()).getFoodDatabaseReference()
//                            .child(String.valueOf(food.getId())).removeValue((error, ref) -> {
//                                Toast.makeText(getActivity(), getString(R.string.msg_delete_food_successfully), Toast.LENGTH_SHORT).show();
//                            });
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    public void getListFoods(String key){
        if(getActivity() == null){
            return;
        }

        MyApplication.get(getActivity()).getFoodDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(foodList != null){
                    foodList.clear();
                }
                else{
                    foodList = new ArrayList<>();
                }

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                    if(food != null){
                        if(StringUtil.isEmpty(key)){
                            foodList.add(0, food);
                        }
                        else{
                            if(GlobalFunction.getTextSearch(food.getName()).toLowerCase().trim()
                                    .contains(GlobalFunction.getTextSearch(key).toLowerCase().trim())){
                                foodList.add(0, food);
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
        fragmentAdminFoodBinding.rcvFood.setLayoutManager(linearLayoutManager);

        AdminFoodAdapter adminFoodAdapter = new AdminFoodAdapter(foodList, new AdminFoodAdapter.IManagerFoodListener() {
            @Override
            public void editFood(Food food) {
                onClickEditFood(food);
            }

            @Override
            public void deleteFood(Food food) {
                deleteFoodItem(food);
            }
        });
        fragmentAdminFoodBinding.rcvFood.setAdapter(adminFoodAdapter);
    }
}