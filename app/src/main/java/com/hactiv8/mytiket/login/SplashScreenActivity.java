package com.hactiv8.mytiket.login;

import static com.hactiv8.mytiket.databinding.ActivitySplashScreenBinding.inflate;
import static com.hactiv8.mytiket.util.LocalPreference.getInstance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hactiv8.mytiket.BuildConfig;
import com.hactiv8.mytiket.MainActivity;
import com.hactiv8.mytiket.R;
import com.hactiv8.mytiket.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ActivitySplashScreenBinding binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String versionName = "Version "+BuildConfig.VERSION_NAME;
        binding.tvVersion.setText(versionName);
        transition();
    }

    private void transition() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::isLogin, 3000);
    }

    private void isLogin(){
        String uid = getInstance(this)
                .getPreferences()
                .getString("uid", "");

        if(currentUser != null && !uid.equals("")) {
            startActivity(new Intent(this, MainActivity.class));
        } else startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}