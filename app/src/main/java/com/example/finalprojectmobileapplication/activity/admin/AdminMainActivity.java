package com.example.finalprojectmobileapplication.activity.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.activity.BaseActivity;
import com.example.finalprojectmobileapplication.databinding.ActivityAdminMainBinding;

import org.greenrobot.eventbus.EventBus;

@SuppressLint("NonConstantResourceId")
public class AdminMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAdminMainBinding activityAdminMainBinding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(activityAdminMainBinding.getRoot());
    }
}
