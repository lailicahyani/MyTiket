package com.hactiv8.mytiket.login;

import static com.hactiv8.mytiket.databinding.ActivityFinishRegristrationBinding.inflate;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hactiv8.mytiket.MainActivity;
import com.hactiv8.mytiket.R;
import com.hactiv8.mytiket.databinding.ActivityFinishRegristrationBinding;

public class FinishRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_regristration);

        ActivityFinishRegristrationBinding binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btnTakeHome.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));
    }
}