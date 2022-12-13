package com.hactiv8.mytiket.dataview;

import static com.hactiv8.mytiket.R.id.ivQrCode;
import static com.hactiv8.mytiket.R.id.tvTitle;
import static com.hactiv8.mytiket.R.layout.layout_qrcode;
import static com.hactiv8.mytiket.util.Constant.getEstimatedTimes;
import static com.hactiv8.mytiket.util.Constant.getQrCode;
import static com.hactiv8.mytiket.util.Constant.getTime;
import static com.hactiv8.mytiket.util.Constant.toUpperCase;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hactiv8.mytiket.api.ReviewsRepository;
import com.hactiv8.mytiket.databinding.ActivityTicketDetailsBinding;
import com.hactiv8.mytiket.pojo.Buses;
import com.hactiv8.mytiket.pojo.Cities;
import com.hactiv8.mytiket.pojo.ScheduleReference;
import com.hactiv8.mytiket.pojo.Transactions;
import com.hactiv8.mytiket.pojo.TransactionsReference;
import com.hactiv8.mytiket.pojo.Users;

public class TicketDetailsActivity extends AppCompatActivity {
    private ActivityTicketDetailsBinding binding;
    private ScheduleReference schedule;
    private Transactions transactions;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(activity_ticket_details);

        binding = ActivityTicketDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TransactionsReference reference = getIntent().getParcelableExtra("transaction");
        Users users = getIntent().getParcelableExtra("user");

        transactions = reference.getTransactions();
        schedule = reference.getReference();
        Buses buses = schedule.getBuses();
        Cities departureCity = schedule.getDeparture();
        Cities arrivalCity = schedule.getArrival();

        binding.tvDate.setText(transactions.getDate());
        binding.tvBookNo.setText("Book No. "+transactions.getBookNo());
        binding.tvName.setText(users.getName());
        binding.tvPhoneNumber.setText(users.getPhoneNumber());
        binding.tvSeats.setText(String.valueOf(transactions.getSeatNo().size()));
        binding.tvStatus.setText(toUpperCase(transactions.getStatus()));
        binding.tvPOName.setText(toUpperCase(buses.getPoName()));
        binding.tvBusNo.setText(buses.getBusNo());
        binding.tvDeparture.setText(toUpperCase(departureCity.getCity()));
        binding.tvTerminalDeparture.setText(toUpperCase(departureCity.getTerminal()));
        binding.tvArrival.setText(toUpperCase(arrivalCity.getCity()));
        binding.tvTerminalArrival.setText(toUpperCase(arrivalCity.getTerminal()));
        binding.tvSeatsNo.setText(transactions.getSeatNo().toString());
        binding.tvTotal.setText(transactions.getTotalPayment());
        binding.ivQrCode.setOnClickListener(this::onShowPopup);
        binding.tvEstimation.setText(getEstimatedTimes(schedule));
        binding.tvDepartureDate.setText(getTime(schedule).get("departureDate"));
        binding.tvArrivalDate.setText(getTime(schedule).get("arrivalDate"));
        binding.tvDepartureTime.setText(getTime(schedule).get("departureTime"));
        binding.tvArrivalTime.setText(getTime(schedule).get("arrivalTime"));

        getUserRating(users);
        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
    }

    private void getUserRating(Users users) {
        String id = schedule.getBuses().getId();
        ReviewsRepository repository = new ReviewsRepository();
        repository.getReviewers(id, users).observe(this, reviewers -> {
            if(reviewers!=null) {
                String reviewer = reviewers.get(0).getRatings()+"/5";
                binding.tvRatings.setText(reviewer);
            }
        });
    }

    public void onShowPopup(View view) {
        View inflatedView = getLayoutInflater().inflate(layout_qrcode, null);
        ImageView imageQr = inflatedView.findViewById(ivQrCode);
        TextView  titleQr = inflatedView.findViewById(tvTitle);
        String title = "Book Number";

        titleQr.setText(title);
        imageQr.setImageBitmap(getQrCode(transactions.getBookNo()));
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(inflatedView);
        builder.show();
    }
}