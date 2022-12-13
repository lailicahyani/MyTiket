package com.hactiv8.mytiket.fragment;

import static com.hactiv8.mytiket.R.drawable.ic_brand;
import static com.hactiv8.mytiket.databinding.FragmentProfileBinding.inflate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.hactiv8.mytiket.databinding.FragmentProfileBinding;
import com.hactiv8.mytiket.login.SettingsActivity;
import com.hactiv8.mytiket.pojo.Users;
import com.hactiv8.mytiket.util.Constant;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentProfileBinding binding = inflate(inflater, container, false);

        Users users = Constant.getUsers(getContext());

        binding.tvName.setText(users.getName());
        binding.tvPhoneNumber.setText(users.getPhoneNumber());
        Glide.with(binding.getRoot())
                .load(users.getPhotoUrl())
                .centerCrop()
                .placeholder(ic_brand)
                .into(binding.ivProfile);

        binding.ivSetting.setOnClickListener(v ->
                startActivity(new Intent(getContext(), SettingsActivity.class)));
        return binding.getRoot();
    }
}