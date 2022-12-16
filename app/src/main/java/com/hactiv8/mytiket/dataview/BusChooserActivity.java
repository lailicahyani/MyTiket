package com.hactiv8.mytiket.dataview;

import static android.R.layout.simple_spinner_dropdown_item;
import static android.view.View.GONE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;
import static android.widget.SeekBar.OnSeekBarChangeListener;
import static com.hactiv8.mytiket.R.array.sort;
import static com.hactiv8.mytiket.R.id;
import static com.hactiv8.mytiket.R.layout;
import static com.hactiv8.mytiket.databinding.ActivityBusChooserBinding.inflate;
import static com.hactiv8.mytiket.util.Constant.getUsers;
import static com.hactiv8.mytiket.util.Constant.toUpperCase;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.HIDDEN;
import static java.lang.Double.compare;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.util.Collections.sort;
import static java.util.Comparator.comparingDouble;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hactiv8.mytiket.adapters.ScheduleAdapter;
import com.hactiv8.mytiket.api.ScheduleRepository;
import com.hactiv8.mytiket.chooser.DatePickerActivity;
import com.hactiv8.mytiket.chooser.DestinationChooserActivity;
import com.hactiv8.mytiket.databinding.ActivityBusChooserBinding;
import com.hactiv8.mytiket.interfaces.ItemClickListener;
import com.hactiv8.mytiket.pojo.Cities;
import com.hactiv8.mytiket.pojo.ScheduleReference;
import com.hactiv8.mytiket.util.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BusChooserActivity extends AppCompatActivity
        implements OnClickListener {
    private ActivityBusChooserBinding binding;
    private Cities departureCity;
    private Cities arrivalCity;
    private Calendar calendar;
    private String passengers;
    private SimpleDateFormat format;
    private ArrayList<ScheduleReference> schedules;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_bus_chooser);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        departureCity = getIntent().getParcelableExtra("departure_city");
        arrivalCity = getIntent().getParcelableExtra("arrival_city");
        passengers = getIntent().getStringExtra("passengers");
        calendar =  (Calendar)getIntent().getSerializableExtra("date");
        format = new SimpleDateFormat("EEE, d MMM yyyy");
        schedules = new ArrayList<>();

        String displayPassengers = "Seat " +passengers;
        binding.tvSeats.setText(displayPassengers);
        binding.tvDeparture.setText(toUpperCase(departureCity.getCity()));
        binding.tvArrival.setText(toUpperCase(arrivalCity.getCity()));
        binding.tvDate.setText(format.format(calendar.getTime()));

        binding.ivBackArrow.setOnClickListener(this);
        binding.llDeparture.setOnClickListener(this);
        binding.llArrival.setOnClickListener(this);
        binding.tvSeats.setOnClickListener(this);
        binding.tvDate.setOnClickListener(this);
        binding.layoutSlidingUp.btnSelected.setOnClickListener(this);
        binding.layoutSlidingUp.btnCancel.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, sort, simple_spinner_dropdown_item);
        binding.tvFilters.setAdapter(adapter);
        binding.tvFilters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) getData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.tvApply.setOnClickListener(v -> getData());
        binding.getRoot().setFadeOnClickListener(view ->
                binding.getRoot().setPanelState(HIDDEN));

        binding.layoutSlidingUp.seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.layoutSlidingUp.tvPassenger.setText(valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(savedInstanceState != null) {
            onStateData(savedInstanceState);
        }else getData();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("schedule", schedules);
        super.onSaveInstanceState(outState);
    }

    private void onStateData(Bundle savedInstanceState) {
        schedules = savedInstanceState.getParcelableArrayList("schedule");
        onSetData(schedules);
    }

    private void getData() {
        binding.layoutError.linearLayout.setVisibility(GONE);
        binding.rvBuses.setVisibility(VISIBLE);
        binding.rvBuses.showShimmerAdapter();

        ScheduleRepository repository = new ScheduleRepository();
        repository.getSchedule(departureCity, arrivalCity, calendar, getUsers(this))
                .observeForever(schedules -> {
                    if(schedules==null) schedules = new ArrayList<>();
                    this.schedules = schedules;
                    onSetData(schedules);
                });
    }

    @SuppressLint({"NewApi", "SetTextI18n"})
    private void onSetData(ArrayList<ScheduleReference> schedules) {

        if (schedules == null) schedules = new ArrayList<>();
        boolean isLowestPiece = binding.tvFilters.getSelectedItem().toString().equals("Lowest price");
        boolean isHighestPiece = binding.tvFilters.getSelectedItem().toString().equals("Highest price");
        boolean isEstimation = binding.tvFilters.getSelectedItem().toString().equals("Estimate time");

        if (isLowestPiece) {
            sort(schedules, comparingDouble(o -> parseDouble(o.getBuses().getPrice())));
        } else if (isHighestPiece) {
            sort(schedules, (o1, o2) ->
                    compare(parseDouble(o2.getBuses().getPrice()),
                            parseDouble(o1.getBuses().getPrice())));
        } else if (isEstimation) {
            sort(schedules, comparingDouble(Constant::getIntEstimatedTimes));
        } else {
            sort(schedules, (o1, o2) ->
                    o2.getReviewers().getRatingsCount()
                            .compareTo(o1.getReviewers().getRatingsCount()));
        }

        String seat = binding.tvSeats.getText().toString().replace("Seat ", "");
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(schedules, parseInt(seat));
        binding.rvBuses.setLayoutManager(new LinearLayoutManager(this));
        binding.rvBuses.setAdapter(scheduleAdapter);
        onListener(scheduleAdapter, schedules);

        if(scheduleAdapter.getItemCount()==0){
            binding.rvBuses.setVisibility(GONE);
            binding.layoutError.textView.setText("Sorry!");
            binding.layoutError.tvMassage.setText("The destination location you selected was not found");
            binding.layoutError.linearLayout.setVisibility(VISIBLE);
        }else {
            binding.layoutError.linearLayout.setVisibility(GONE);
            binding.rvBuses.setVisibility(VISIBLE);
        }
    }

    private void onListener(ScheduleAdapter scheduleAdapter, ArrayList<ScheduleReference> schedules) {
        scheduleAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(view.getId()== id.btnBookNow){
                    startActivity(new Intent(getApplicationContext(),
                            BusDetailsActivity.class)
                            .putExtra("schedule", schedules.get(position))
                            .putExtra("date", calendar)
                            .putExtra("passengers", passengers));
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, DestinationChooserActivity.class);
        switch (v.getId()){
            case id.ivBackArrow:
                onBackPressed();
                break;
            case id.llDeparture:
                intent.putExtra("hint", binding.tvDeparture.getText().toString());
                startActivityForResult(intent, 1);
                break;
            case id.llArrival:
                intent.putExtra("hint", binding.tvArrival.getText().toString());
                startActivityForResult(intent, 2);
                break;
            case id.tvDate:
                startActivityForResult(new Intent(this, DatePickerActivity.class), 3);
                break;
            case id.tvSeats:
                binding.slidingLayout.setPanelState(EXPANDED);
                break;
            case id.btnSelected:
                String passenger = "Seat "+binding.layoutSlidingUp.tvPassenger.getText();
                binding.tvSeats.setText(passenger);
                binding.getRoot().setPanelState(HIDDEN);
                break;
            case id.btnCancel:
                binding.getRoot().setPanelState(HIDDEN);
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (data != null && resultCode == RESULT_OK) {
                    departureCity = data.getParcelableExtra("city");
                    binding.tvDeparture.setText(toUpperCase(departureCity.getCity()));
//                    dep = departureCity.getCity();
                }
                break;
            case 2:
                if (data != null && resultCode == RESULT_OK) {
                    arrivalCity = data.getParcelableExtra("city");
                    binding.tvArrival.setText(toUpperCase(arrivalCity.getCity()));
                }
                break;
            case 3:
                if (data != null && resultCode == RESULT_OK) {
                    calendar = (Calendar) data.getSerializableExtra("date");
                    binding.tvDate.setText(format.format(calendar.getTime()));
                }
                break;
        }
    }
}