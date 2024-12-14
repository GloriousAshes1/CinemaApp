package com.example.finalprojectmobileapplication.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.databinding.ActivityChangePasswordBinding;
import com.example.finalprojectmobileapplication.model.User;
import com.example.finalprojectmobileapplication.prefs.DataStoreManager;
import com.example.finalprojectmobileapplication.util.StringUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding mActivityChangePasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityChangePasswordBinding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(mActivityChangePasswordBinding.getRoot());
        mActivityChangePasswordBinding.imgBack.setOnClickListener(v -> finish());
        mActivityChangePasswordBinding.btnChangePassword.setOnClickListener(v -> onClickValidateChangePassword());
    }

    private void onClickValidateChangePassword(){
        String oldPassword = mActivityChangePasswordBinding.edtOldPassword.getText().toString().trim();
        String newPassword = mActivityChangePasswordBinding.edtNewPassword.getText().toString().trim();
        String confirmPassword = mActivityChangePasswordBinding.edtConfirmPassword.getText().toString().trim();
        if(StringUtil.isEmpty(oldPassword)){
            Toast.makeText(this, getString(R.string.msg_old_password_require), Toast.LENGTH_SHORT).show();
        }
        else if(StringUtil.isEmpty(newPassword)){
            Toast.makeText(this, getString(R.string.msg_new_password_require), Toast.LENGTH_SHORT).show();
        }
        else if(StringUtil.isEmpty(confirmPassword)){
            Toast.makeText(this, getString(R.string.msg_confirm_password_require), Toast.LENGTH_SHORT).show();
        }
        else if(!DataStoreManager.getUser().getPassword().equals(oldPassword)){
            Toast.makeText(this, getString(R.string.msg_old_password_invalid), Toast.LENGTH_SHORT).show();
        }
        else if(!newPassword.equals(confirmPassword)){
            Toast.makeText(this, getString(R.string.msg_confirm_password_invalid), Toast.LENGTH_SHORT).show();
        } else if (newPassword.equals(oldPassword)) {
            Toast.makeText(this, getString(R.string.msg_new_password_invalid), Toast.LENGTH_SHORT).show();
        }else {
            changePassword(newPassword);
        }
    }

    private void changePassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        user.updatePassword(newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, getString(R.string.msg_change_password_successfully), Toast.LENGTH_SHORT).show();
                User userLogin =DataStoreManager.getUser();
                userLogin.setPassword(newPassword);
                DataStoreManager.setUser(userLogin);
                mActivityChangePasswordBinding.edtOldPassword.setText("");
                mActivityChangePasswordBinding.edtNewPassword.setText("");
                mActivityChangePasswordBinding.edtConfirmPassword.setText("");
                finish();
            }
        });
    }
}