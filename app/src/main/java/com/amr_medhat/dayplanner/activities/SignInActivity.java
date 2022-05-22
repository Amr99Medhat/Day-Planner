package com.amr_medhat.dayplanner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.amr_medhat.dayplanner.database.PreferenceManager;
import com.amr_medhat.dayplanner.database.SQLiteRegistrationManager;
import com.amr_medhat.dayplanner.databinding.ActivitySignInBinding;
import com.amr_medhat.dayplanner.utilities.Constants;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding activitySignInBinding;
    SQLiteRegistrationManager sqLiteRegistrationManager;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(activitySignInBinding.getRoot());
        preferenceManager = new PreferenceManager(this);
        sqLiteRegistrationManager = SQLiteRegistrationManager.instanceOfDatabase(this);
        checkIfSignedIn();
        setListeners();
    }


    private void checkIfSignedIn() {
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void setListeners() {
        activitySignInBinding.btnSignIn.setOnClickListener(v -> {
            String username = Objects.requireNonNull(activitySignInBinding.edUsername.getText()).toString();
            String userPassword = Objects.requireNonNull(activitySignInBinding.edPassword.getText()).toString();
            if (username.equals("")) {
                activitySignInBinding.edUsername.setError("Enter username");
                activitySignInBinding.edUsername.requestFocus();
            } else if (userPassword.equals("")) {
                activitySignInBinding.edPassword.setError("Enter password");
                activitySignInBinding.edPassword.requestFocus();
            } else {
                Boolean checkUsernameAndPassword = sqLiteRegistrationManager.checkUserNameAndPassword(username, userPassword);
                if (checkUsernameAndPassword) {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USERNAME, username);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        activitySignInBinding.btnSignUp.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        });

        activitySignInBinding.tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));
        });
    }
}