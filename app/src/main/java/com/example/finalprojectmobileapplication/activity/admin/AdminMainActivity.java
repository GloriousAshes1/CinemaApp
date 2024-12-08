package com.example.finalprojectmobileapplication.activity.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.activity.BaseActivity;
import com.example.finalprojectmobileapplication.adapter.admin.AdminViewPagerAdapter;
import com.example.finalprojectmobileapplication.databinding.ActivityAdminMainBinding;
import com.example.finalprojectmobileapplication.event.ResultQrCodeEvent;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;

@SuppressLint("NonConstantResourceId")
public class AdminMainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAdminMainBinding adminMainBinding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(adminMainBinding.getRoot());

        AdminViewPagerAdapter adminViewPagerAdapter = new AdminViewPagerAdapter(this);
        adminMainBinding.viewpager2.setAdapter(adminViewPagerAdapter);

        //Hàm để cho phép kéo màn hình qua lại
        adminMainBinding.viewpager2.setUserInputEnabled(true);

        adminMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position){
                    case 0:
                        adminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_admin_category).setChecked(true);
                        adminMainBinding.tvTitle.setText(getString(R.string.nav_admin_category));
                        break;

                    case 1:
                        adminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_admin_food_drink).setChecked(true);
                        adminMainBinding.tvTitle.setText(getString(R.string.nav_admin_food_drink));
                        break;

                    case 2:
                        adminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_admin_movie).setChecked(true);
                        adminMainBinding.tvTitle.setText(getString(R.string.nav_admin_movie));
                        break;

                    case 3:
                        adminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_admin_booking).setChecked(true);
                        adminMainBinding.tvTitle.setText(getString(R.string.nav_admin_booking));
                        break;

                    case 4:
                        adminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_admin_manage).setChecked(true);
                        adminMainBinding.tvTitle.setText(getString(R.string.nav_admin_manage));
                        break;
                }
            }
        });

        adminMainBinding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            switch (id){
                case R.id.nav_admin_category:
                    adminMainBinding.viewpager2.setCurrentItem(0);
                    adminMainBinding.tvTitle.setText(getString(R.string.nav_admin_category));
                    break;

                case R.id.nav_admin_food_drink:
                    adminMainBinding.viewpager2.setCurrentItem(1);
                    adminMainBinding.tvTitle.setText(getString(R.string.nav_admin_food_drink));
                    break;

                case R.id.nav_admin_movie:
                    adminMainBinding.viewpager2.setCurrentItem(2);
                    adminMainBinding.tvTitle.setText(getString(R.string.nav_admin_movie));
                    break;

                case R.id.nav_admin_booking:
                    adminMainBinding.viewpager2.setCurrentItem(3);
                    adminMainBinding.tvTitle.setText(getString(R.string.nav_admin_booking));
                    break;

                case R.id.nav_admin_manage:
                    adminMainBinding.viewpager2.setCurrentItem(4);
                    adminMainBinding.tvTitle.setText(getString(R.string.nav_admin_manage));
                    break;
            }
            return true;
        });
    }

    private void showDialogLogout(){
        new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content(getString(R.string.msg_confirm_login_another_device))
                .positiveText(getString(R.string.action_ok))
                .negativeText(getString(R.string.action_cancel))
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    finishAffinity();
                })
                .onNegative((dialog, which) -> {
                    dialog.dismiss();
                })
                .cancelable(true)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult != null && intentResult.getContents() != null){
            EventBus.getDefault().post(new ResultQrCodeEvent(intentResult.getContents()));
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        showDialogLogout();
    }
}