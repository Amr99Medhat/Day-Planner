package com.amr_medhat.dayplanner.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.amr_medhat.dayplanner.databinding.ActivitySplashScreenBinding;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding activitySplashScreenBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashScreenBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(activitySplashScreenBinding.getRoot());
        setAnimation();
        moveToMainActivity();
    }


    private void setAnimation() {
        //first move
        activitySplashScreenBinding.ivAppLogo.animate().translationY(1000).setDuration(1000);
        activitySplashScreenBinding.ivAppName.animate().translationY(-1100).setDuration(1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //second move
                activitySplashScreenBinding.ivAppName.animate().translationY(1800).setDuration(1000);
                activitySplashScreenBinding.ivAppLogo.animate().translationY(3000).setDuration(1000);
                activitySplashScreenBinding.ivBackground.animate().translationY(-3000).setDuration(1200);

            }
        }, 2000);
    }

    private void moveToMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
            }
        }, 2900);
    }
}

