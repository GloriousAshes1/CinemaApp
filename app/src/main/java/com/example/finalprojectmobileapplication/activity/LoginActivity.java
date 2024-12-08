package com.example.finalprojectmobileapplication.activity;

import static com.example.finalprojectmobileapplication.constant.ConstantKey.ADMIN_EMAIL_FORMAT;

import android.os.Bundle;
import android.widget.Toast;


import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.constant.GlobalFunction;
import com.example.finalprojectmobileapplication.databinding.ActivityLoginBinding;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectmobileapplication.model.User;
import com.example.finalprojectmobileapplication.prefs.DataStoreManager;
import com.example.finalprojectmobileapplication.util.StringUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        activityLoginBinding.rdbUser.setChecked(true);

        activityLoginBinding.layoutSignUp.setOnClickListener(
                v -> GlobalFunction.startActivity(LoginActivity.this, SignUpActivity.class));

        activityLoginBinding.btnLogin.setOnClickListener(v -> onClickValidateSignIn());
        activityLoginBinding.tvForgotPassword.setOnClickListener(v -> onClickForgotPassword());
    }
    private void onClickForgotPassword() {
        GlobalFunction.startActivity(this, ForgotPasswordActivity.class);
    }

    private void onClickValidateSignIn() {
        String strEmail = activityLoginBinding.edtEmail.getText().toString().trim();
        String strPassword = activityLoginBinding.edtPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(LoginActivity.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(LoginActivity.this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(LoginActivity.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            if (activityLoginBinding.rdbAdmin.isChecked()) {
                if (!strEmail.contains(ADMIN_EMAIL_FORMAT)) {
                    Toast.makeText(LoginActivity.this, getString(R.string.msg_email_invalid_admin), Toast.LENGTH_SHORT).show();
                } else {
                    signInUser(strEmail, strPassword);
                }
                return;
            }

            if (strEmail.contains(ADMIN_EMAIL_FORMAT)) {
                Toast.makeText(LoginActivity.this, getString(R.string.msg_email_invalid_user), Toast.LENGTH_SHORT).show();
            } else {
                signInUser(strEmail, strPassword);
            }
        }
    }

    private void signInUser(String email, String password) {
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            User userObject = new User(user.getEmail(), password);
                            if (user.getEmail() != null && user.getEmail().contains(ADMIN_EMAIL_FORMAT)) {
                                userObject.setAdmin(true);
                            }
                            DataStoreManager.setUser(userObject);
                            GlobalFunction.gotoMainActivity(this);
                            finishAffinity();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.msg_sign_in_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}