package com.example.finalprojectmobileapplication.activity;

import static com.example.finalprojectmobileapplication.constant.ConstantKey.ADMIN_EMAIL_FORMAT;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.constant.GlobalFunction;
import com.example.finalprojectmobileapplication.databinding.ActivitySignUpBinding;
import com.example.finalprojectmobileapplication.model.User;
import com.example.finalprojectmobileapplication.prefs.DataStoreManager;
import com.example.finalprojectmobileapplication.util.StringUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding mActivitySignUpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignUpBinding.getRoot());

        mActivitySignUpBinding.rdbUser.setChecked(true);

        mActivitySignUpBinding.imgBack.setOnClickListener(v -> onBackPressed());
        mActivitySignUpBinding.layoutSignIn.setOnClickListener(v -> finish());
        mActivitySignUpBinding.btnSignUp.setOnClickListener(v -> onClickValidateSignUp());
    }

    private void onClickValidateSignUp() {
        String strEmail = mActivitySignUpBinding.edtEmail.getText().toString().trim();
        String strPassword = mActivitySignUpBinding.edtPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            if (mActivitySignUpBinding.rdbAdmin.isChecked()) {
                if (!strEmail.contains(ADMIN_EMAIL_FORMAT)) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid_admin), Toast.LENGTH_SHORT).show();
                } else {
                    signUpUser(strEmail, strPassword);
                }
                return;
            }

            if (strEmail.contains(ADMIN_EMAIL_FORMAT)) {
                Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid_user), Toast.LENGTH_SHORT).show();
            } else {
                signUpUser(strEmail, strPassword);
            }
        }
    }

    private void signUpUser(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
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
                        Toast.makeText(SignUpActivity.this, getString(R.string.msg_sign_up_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}