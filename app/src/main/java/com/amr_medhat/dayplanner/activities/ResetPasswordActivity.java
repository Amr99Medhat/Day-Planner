package com.amr_medhat.dayplanner.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amr_medhat.dayplanner.R;
import com.amr_medhat.dayplanner.database.PreferenceManager;
import com.amr_medhat.dayplanner.database.SQLiteRegistrationManager;
import com.amr_medhat.dayplanner.databinding.ActivityResetPasswordBinding;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {
    ActivityResetPasswordBinding activityResetPasswordBinding;
    SQLiteRegistrationManager sqLiteRegistrationManager;
    PreferenceManager preferenceManager;
    int state = 0;
    String username;
    String verifiedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityResetPasswordBinding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(activityResetPasswordBinding.getRoot());
        sqLiteRegistrationManager = new SQLiteRegistrationManager(this);
        preferenceManager = new PreferenceManager(this);
        setListeners();
    }

    @SuppressLint("SetTextI18n")
    private void setListeners() {
        activityResetPasswordBinding.btnResetPassword.setOnClickListener(v -> {
            if (state == 0) {
                username = Objects.requireNonNull(activityResetPasswordBinding.edUsername.getText()).toString();
                verifiedCode = Objects.requireNonNull(activityResetPasswordBinding.edVerifiedCode.getText()).toString();
                if (username.equals("")) {
                    activityResetPasswordBinding.edUsername.setError("Enter username");
                    activityResetPasswordBinding.edUsername.requestFocus();
                } else if (verifiedCode.equals("")) {
                    activityResetPasswordBinding.edVerifiedCode.setError("Enter verified code");
                    activityResetPasswordBinding.edVerifiedCode.requestFocus();
                } else {
                    Boolean checkUsername = sqLiteRegistrationManager.checkUserName(username);
                    Boolean checkUserVerificationCode = sqLiteRegistrationManager.checkUserVerificationCode(username, verifiedCode);

                    if (checkUsername) {
                        activityResetPasswordBinding.edUsername.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                        activityResetPasswordBinding.TILUsername.setEnabled(false);
                        activityResetPasswordBinding.edUsername.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_text_secondary));
                        activityResetPasswordBinding.edUsername.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check), null);
                    } else {
                        activityResetPasswordBinding.edUsername.setError("Doesn't Exist");
                        activityResetPasswordBinding.edUsername.requestFocus();
                        return;
                    }

                    if (checkUserVerificationCode) {
                        activityResetPasswordBinding.edVerifiedCode.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        activityResetPasswordBinding.TILVerifiedCode.setEnabled(false);
                        activityResetPasswordBinding.edVerifiedCode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_text_secondary));
                        activityResetPasswordBinding.edVerifiedCode.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check), null);

                    } else {
                        activityResetPasswordBinding.edVerifiedCode.setError("Wrong Code");
                        activityResetPasswordBinding.edVerifiedCode.requestFocus();
                        return;
                    }
                    activityResetPasswordBinding.TILPassword.setEnabled(true);
                    activityResetPasswordBinding.TILPassword.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.white)));
                    activityResetPasswordBinding.TILConfirmPassword.setEnabled(true);
                    activityResetPasswordBinding.TILConfirmPassword.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.white)));
                    activityResetPasswordBinding.btnResetPassword.setText("Update Password");
                    state = 1;

                }
            } else {
                // update
                String userPassword = Objects.requireNonNull(activityResetPasswordBinding.edPassword.getText()).toString();
                String userPasswordConfirmation = Objects.requireNonNull(activityResetPasswordBinding.edConfirmPassword.getText()).toString();
                if (userPassword.equals("")) {
                    activityResetPasswordBinding.edPassword.setError("Enter password");
                    activityResetPasswordBinding.edPassword.requestFocus();
                } else if (userPasswordConfirmation.equals("")) {
                    activityResetPasswordBinding.edConfirmPassword.setError("Enter password confirmation");
                    activityResetPasswordBinding.edConfirmPassword.requestFocus();
                } else if (userPassword.equals(userPasswordConfirmation)) {
                    int update = sqLiteRegistrationManager.updatePassword(username, userPassword, verifiedCode);
                    if (update > 0) {
                        preferenceManager.clear();
                        onBackPressed();
                    } else {
                        Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Passwords are not matching", Toast.LENGTH_SHORT).show();
                }
            }

        });

        activityResetPasswordBinding.btnSignIn.setOnClickListener(v -> {
            onBackPressed();
        });
    }

}