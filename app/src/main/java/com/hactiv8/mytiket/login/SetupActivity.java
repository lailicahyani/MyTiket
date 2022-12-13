package com.hactiv8.mytiket.login;

import static com.google.android.material.snackbar.Snackbar.LENGTH_SHORT;
import static com.google.android.material.snackbar.Snackbar.make;
import static com.hactiv8.mytiket.databinding.ActivitySetupBinding.inflate;
import static com.hactiv8.mytiket.util.LocalPreference.getInstance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hactiv8.mytiket.R;
import com.hactiv8.mytiket.api.UsersRepository;
import com.hactiv8.mytiket.databinding.ActivitySetupBinding;
import com.hactiv8.mytiket.pojo.Users;


public class SetupActivity extends AppCompatActivity {
    private ActivitySetupBinding binding;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);

        binding.btnSaveNumber.setOnClickListener(v -> finishSetup());
    }

    private void finishSetup() {
        String phoneNumber = String.valueOf(binding.etPhoneNumber.getText());
        if(!phoneNumber.isEmpty()){
            dialog.setMessage("currently preparing your profile..");
            dialog.setCancelable(false);
            dialog.show();

            Users user = getIntent().getParcelableExtra("user");
            user.setPhoneNumber(phoneNumber);

            new UsersRepository().insertUser(user).addOnSuccessListener(documentReference -> {
              getInstance(this).getEditor()
                        .putString("uid", user.getUid())
                        .putString("name", user.getName())
                        .putString("email", user.getEmail())
                        .putString("photo", user.getPhotoUrl())
                        .putString("phone", user.getPhoneNumber()).apply();

                startActivity(new Intent(this, FinishRegistrationActivity.class)
                        .putExtra("user", user));
                dialog.dismiss();
                finishAffinity();
            }).addOnFailureListener(e -> {
                dialog.dismiss();
                make(binding.getRoot(),
                        "Error adding document.",
                        LENGTH_SHORT).show();
            });

        }else{
            dialog.dismiss();
            make(binding.getRoot(),
                    "Please enter your phone number",
                    LENGTH_SHORT).show();
        }
    }
}
