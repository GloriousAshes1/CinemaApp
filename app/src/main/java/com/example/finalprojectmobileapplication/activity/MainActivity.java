package com.example.finalprojectmobileapplication.activity;

import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.adapter.MyViewPagerAdapter;
import com.example.finalprojectmobileapplication.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(this);
        activityMainBinding.viewpager2.setAdapter(myViewPagerAdapter);
        activityMainBinding.viewpager2.setUserInputEnabled(false);

        activityMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch(position){
                    case 0:
                        activityMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
                        activityMainBinding.tvTitle.setText(getString(R.string.nav_home));
                        break;
                    case 1:
                        activityMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_booking).setChecked(true);
                        activityMainBinding.tvTitle.setText(getString(R.string.nav_booking));
                        break;

                    case 2:
                        activityMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_user).setChecked(true);
                        activityMainBinding.tvTitle.setText(getString(R.string.nav_user));
                        break;
                }
            }
        });

        activityMainBinding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                activityMainBinding.viewpager2.setCurrentItem(0);
                activityMainBinding.tvTitle.setText(getString(R.string.nav_home));
            } else if (id == R.id.nav_booking) {
                activityMainBinding.viewpager2.setCurrentItem(1);
                activityMainBinding.tvTitle.setText(getString(R.string.nav_booking));
            } else if (id == R.id.nav_user) {
                activityMainBinding.viewpager2.setCurrentItem(2);
                activityMainBinding.tvTitle.setText(getString(R.string.nav_user));
            }
            return true;
        });
    }

    private void showDialogLogout() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content(getString(R.string.msg_confirm_login_another_device))
                .positiveText(getString(R.string.action_ok))
                .negativeText(getString(R.string.action_cancel))
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    finishAffinity();
                })
                .onNegative((dialog, which) -> dialog.dismiss())
                .cancelable(true)
                .show();
    }

    @Override
    public void onBackPressed() {
        showDialogLogout();
    }
}