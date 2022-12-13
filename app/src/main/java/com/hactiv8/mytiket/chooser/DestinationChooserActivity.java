package com.hactiv8.mytiket.chooser;

import static com.hactiv8.mytiket.databinding.ActivityDestinationChooserBinding.inflate;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hactiv8.mytiket.R;
import com.hactiv8.mytiket.adapters.CitiesAdapter;
import com.hactiv8.mytiket.databinding.ActivityDestinationChooserBinding;
import com.hactiv8.mytiket.interfaces.ItemClickListener;
import com.hactiv8.mytiket.pojo.Cities;
import com.hactiv8.mytiket.util.MainViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class DestinationChooserActivity extends AppCompatActivity {
    private ActivityDestinationChooserBinding binding;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_chooser);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        getHint();
        getData();

        binding.etSearch.setFocusable(true);
        binding.tvCancel.setOnClickListener(v -> onBackPressed());
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getData(binding.etSearch.getText()
                        .toString()
                        .toLowerCase(Locale.ROOT));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getHint() {
        String hint = getIntent().getStringExtra("hint");
        if(!hint.contains("Try")){
            hint = "Try \""+hint+"\"";
        }
        binding.etSearch.setHint(hint);
    }

    private void getData() {
        mainViewModel.getCities().observe(this, this::onDataChanged);
    }

    private void getData(String city) {
        mainViewModel.getCities("city", city).observe(this, this::onDataChanged);
    }

    private void onListener(CitiesAdapter productsAdapter, ArrayList<Cities> citiesList) {
        productsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                setResult(RESULT_OK, new Intent()
                        .putExtra("city", citiesList.get(position)));
                onBackPressed();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    private void onDataChanged(ArrayList<Cities> cities) {
        if(cities==null) cities = new ArrayList<>();
        CitiesAdapter citiesAdapter = new CitiesAdapter(cities);
        binding.rvSearch.showShimmerAdapter();
        binding.rvSearch.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSearch.setAdapter(citiesAdapter);

        onListener(citiesAdapter, cities);
    }
}