package com.hactiv8.mytiket.payment;

import static com.hactiv8.mytiket.databinding.ActivityDetailPaymentBinding.inflate;
import static com.hactiv8.mytiket.util.Constant.getEstimatedTimes;
import static com.hactiv8.mytiket.util.Constant.getPieces;
import static com.hactiv8.mytiket.util.Constant.getTime;
import static com.hactiv8.mytiket.util.Constant.getUsers;
import static com.hactiv8.mytiket.util.Constant.toUpperCase;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.lang.String.valueOf;
import static java.util.Collections.sort;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hactiv8.mytiket.R;
import com.hactiv8.mytiket.databinding.ActivityDetailPaymentBinding;
import com.hactiv8.mytiket.databinding.LayoutCompleteBookingBinding;
import com.hactiv8.mytiket.pojo.Buses;
import com.hactiv8.mytiket.pojo.Cities;
import com.hactiv8.mytiket.pojo.ScheduleReference;
import com.hactiv8.mytiket.pojo.Transactions;
import com.hactiv8.mytiket.pojo.Users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class DetailPaymentActivity extends AppCompatActivity {
    private LayoutCompleteBookingBinding bookingBinding;
    private ArrayList<String> seatChooser;
    private ScheduleReference schedule;
    private Users users;
    private Transactions transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_payment);

        ActivityDetailPaymentBinding binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        users = getUsers(this);
        transactions = new Transactions();
        schedule = getIntent().getParcelableExtra("schedule");
        String passengers = getIntent().getStringExtra("passengers");
        seatChooser = getIntent().getStringArrayListExtra("seats");
        Cities departureCity = schedule.getDeparture();
        Cities arrivalCity = schedule.getArrival();
        Buses buses = schedule.getBuses();
        sort(seatChooser, CASE_INSENSITIVE_ORDER);

        bookingBinding = binding.layoutCompleteBooking;
        bookingBinding.tvSeatsNo.setText(valueOf(seatChooser));
        bookingBinding.tvSeats.setText(passengers);
        bookingBinding.tvPOName.setText(toUpperCase(buses.getPoName()));
        bookingBinding.tvBusNo.setText(buses.getBusNo());
        bookingBinding.tvDeparture.setText(toUpperCase(departureCity.getCity()));
        bookingBinding.tvTerminalDeparture.setText(toUpperCase(departureCity.getTerminal()));
        bookingBinding.tvArrival.setText(toUpperCase(arrivalCity.getCity()));
        bookingBinding.tvTerminalArrival.setText(toUpperCase(arrivalCity.getTerminal()));
        binding.layoutCompleteBooking.tvEstimation.setText(getEstimatedTimes(schedule));
        bookingBinding.tvDepartureDate.setText(getTime(schedule).get("departureDate"));
        bookingBinding.tvArrivalDate.setText(getTime(schedule).get("arrivalDate"));
        bookingBinding.tvDepartureTime.setText(getTime(schedule).get("departureTime"));
        bookingBinding.tvArrivalTime.setText(getTime(schedule).get("arrivalTime"));
        bookingBinding.tvTickets.setText(getPieces(buses, passengers).get("displaySubTotal"));
        bookingBinding.tvTotals.setText(getPieces(buses, passengers).get("subTotal"));
        bookingBinding.tvName.setText(users.getName());
        bookingBinding.tvPhoneNumber.setText(users.getPhoneNumber());

        Random random = new Random();
        int firstCode = random.nextInt(999999);
        int secondCode = random.nextInt(9999);
        int uniqueCode = random.nextInt(999);

        String bookNo = firstCode+"-"+secondCode;
        bookingBinding.tvUniqueCode.setText(valueOf(uniqueCode));
        transactions.setUniqueCode(valueOf(uniqueCode));
        transactions.setBookNo(bookNo);

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btmContinue.setOnClickListener(this::onClickContinue);
    }

    @SuppressLint("SimpleDateFormat")
    private void onClickContinue(View v) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        Date date = new Date();

        transactions.setDate(valueOf(format.format(date.getTime())));
        transactions.setUid(users.getUid());
        transactions.setSchedule(schedule.getId());
        transactions.setSeatNo(seatChooser);
        transactions.setStatus("issued");
        transactions.setTotalPayment(bookingBinding.tvTotals.getText().toString());

        startActivity(new Intent(this,
                PaymentActivity.class)
                .putExtra("transactions", transactions)
                .putExtra("schedule", schedule));
    }
}