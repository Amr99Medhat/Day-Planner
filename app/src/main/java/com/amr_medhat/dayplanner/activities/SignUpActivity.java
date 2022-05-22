package com.amr_medhat.dayplanner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.amr_medhat.dayplanner.database.PreferenceManager;
import com.amr_medhat.dayplanner.database.SQLiteRegistrationManager;
import com.amr_medhat.dayplanner.databinding.ActivitySignUpBinding;
import com.amr_medhat.dayplanner.utilities.Constants;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding activitySignUpBinding;
    SQLiteRegistrationManager sqLiteRegistrationManager;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());
        preferenceManager = new PreferenceManager(this);
        sqLiteRegistrationManager = SQLiteRegistrationManager.instanceOfDatabase(this);
        setListeners();

    }

    private void setListeners() {
        activitySignUpBinding.btnSignUp.setOnClickListener(v -> {
            String username = Objects.requireNonNull(activitySignUpBinding.edUsername.getText()).toString();
            String userPassword = Objects.requireNonNull(activitySignUpBinding.edPassword.getText()).toString();
            String userPasswordConfirmation = Objects.requireNonNull(activitySignUpBinding.edConfirmPassword.getText()).toString();
            String verifiedCode = Objects.requireNonNull(activitySignUpBinding.edVerifiedCode.getText()).toString();

            if (username.equals("")) {
                activitySignUpBinding.edUsername.setError("Enter username");
                activitySignUpBinding.edUsername.requestFocus();
            } else if (userPassword.equals("")) {
                activitySignUpBinding.edPassword.setError("Enter password");
                activitySignUpBinding.edPassword.requestFocus();
            } else if (userPasswordConfirmation.equals("")) {
                activitySignUpBinding.edConfirmPassword.setError("Confirm password");
                activitySignUpBinding.edConfirmPassword.requestFocus();
            } else if (!userPassword.equals(userPasswordConfirmation)) {
                Toast.makeText(getApplicationContext(), "Passwords are not matching", Toast.LENGTH_SHORT).show();
            } else if (verifiedCode.equals("")) {
                activitySignUpBinding.edVerifiedCode.setError("Enter verified code");
                activitySignUpBinding.edVerifiedCode.requestFocus();
            } else {
                Boolean checkUser = sqLiteRegistrationManager.checkUserName(username);
                if (!checkUser) {
                    Boolean insert = sqLiteRegistrationManager.addUser(username, userPassword, verifiedCode);
                    if (insert) {
                        Toast.makeText(getApplicationContext(), "Registration Successfully", Toast.LENGTH_SHORT).show();
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USERNAME, username);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "User Already Exist", Toast.LENGTH_SHORT).show();
                }
            }

        });
        activitySignUpBinding.btnSignIn.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}