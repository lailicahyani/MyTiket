package com.hactiv8.mytiket.fragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.hactiv8.mytiket.util.Constant.getUsers;
import static com.hactiv8.mytiket.util.LocalPreference.getInstance;
import static java.lang.String.valueOf;
import static java.util.Collections.sort;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hactiv8.mytiket.R;
import com.hactiv8.mytiket.adapters.TicketsAdapter;
import com.hactiv8.mytiket.api.ReviewsRepository;
import com.hactiv8.mytiket.databinding.FragmentTicketsBinding;
import com.hactiv8.mytiket.dataview.TicketDetailsActivity;
import com.hactiv8.mytiket.interfaces.ItemClickListener;
import com.hactiv8.mytiket.pojo.Reviewers;
import com.hactiv8.mytiket.pojo.ScheduleReference;
import com.hactiv8.mytiket.pojo.Transactions;
import com.hactiv8.mytiket.pojo.TransactionsReference;
import com.hactiv8.mytiket.pojo.Users;
import com.hactiv8.mytiket.util.MainViewModel;

import java.util.ArrayList;

public class TicketsFragment extends Fragment {
    private FragmentTicketsBinding binding;
    ArrayList<TransactionsReference> ticket;
    Users users;

    public TicketsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTicketsBinding.inflate(inflater, container, false);
        users = getUsers(getContext());

        ScheduleReference schedule = getArguments().getParcelable("schedule");
        Transactions transactions = getArguments().getParcelable("transactions");
        boolean isRating = getInstance(getContext())
                .getPreferences()
                .getBoolean("isRating", false);
        if(isRating) getPopup(schedule, transactions, users);

        if(savedInstanceState != null) {
            onStateData(savedInstanceState);
        }else getData();

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("ticket", ticket);
        super.onSaveInstanceState(outState);
    }

    private void onStateData(Bundle savedInstanceState) {
        ticket = savedInstanceState.getParcelableArrayList("ticket");
        onSetData(ticket);
    }

    private void getData() {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getTransactions(users.getUid()).observe(getViewLifecycleOwner(), ticket -> {
            if (ticket == null) ticket = new ArrayList<>();
            this.ticket = ticket;
            onSetData(ticket);
        });
    }

    private void onSetData(ArrayList<TransactionsReference> ticket) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sort(ticket, (o1, o2) -> o2.getTransactions().getDate()
                    .compareTo(o1.getTransactions().getDate()));
        }
        TicketsAdapter citiesAdapter = new TicketsAdapter(ticket);
        binding.rvTickets.setVisibility(VISIBLE);
        binding.layoutError.linearLayout.setVisibility(GONE);
        binding.rvTickets.showShimmerAdapter();
        binding.rvTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTickets.setAdapter(citiesAdapter);
        onListener(citiesAdapter, ticket, users);

        if(citiesAdapter.getItemCount()==0){
            binding.rvTickets.setVisibility(GONE);
            binding.layoutError.linearLayout.setVisibility(VISIBLE);
        }
    }

    @SuppressLint("InflateParams")
    public void getPopup(ScheduleReference schedule, Transactions transactions, Users users) {
        View layoutRating = getLayoutInflater().inflate(R.layout.layout_rating, null);
        TextView poName = layoutRating.findViewById(R.id.tvPOName);
        TextView busNo = layoutRating.findViewById(R.id.tvBusNo);
        TextView departureDate = layoutRating.findViewById(R.id.tvDepartureDate);
        TextView status = layoutRating.findViewById(R.id.tvStatus);
        TextView maxLine = layoutRating.findViewById(R.id.tvMaxLine);
        EditText content = layoutRating.findViewById(R.id.etContent);
        CheckedTextView star1 = layoutRating.findViewById(R.id.ctvStar1);
        CheckedTextView star2 = layoutRating.findViewById(R.id.ctvStar2);
        CheckedTextView star3 = layoutRating.findViewById(R.id.ctvStar3);
        CheckedTextView star4 = layoutRating.findViewById(R.id.ctvStar4);
        CheckedTextView star5 = layoutRating.findViewById(R.id.ctvStar5);
        String id = schedule.getBuses().getId();

        poName.setText(schedule.getBuses().getPoName());
        busNo.setText(schedule.getBuses().getBusNo());
        departureDate.setText(schedule.getDepartureTime());
        status.setText(transactions.getStatus());

        ReviewsRepository repository = new ReviewsRepository();
        repository.getReviewers(id, users).observe(getViewLifecycleOwner(), reviewers -> {
            if(reviewers==null) {
                onShowPopup(transactions, users, layoutRating, maxLine,
                        content, star1, star2, star3, star4, star5, id, repository);
            }

            getInstance(getContext()).getEditor()
                    .putBoolean("isRating", false)
                    .apply();
        });
    }

    private void onShowPopup(Transactions transactions, Users users, View inflatedView,
                             TextView maxLine, EditText content, CheckedTextView star1,
                             CheckedTextView star2, CheckedTextView star3, CheckedTextView star4,
                             CheckedTextView star5, String id, ReviewsRepository repository) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setView(inflatedView);
        builder.setCancelable(false);

        star1.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                !star1.isChecked(), false, false, false, false));
        star2.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, false, false, false));
        star3.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, true, false, false));
        star4.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, true, true, false));
        star5.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, true, true, true));

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count<=500){
                    String indicator = s.length()+"/500";
                    maxLine.setText(indicator);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setPositiveButton("Rate This Trip", (dialog, which) -> {
            int rating = 0;
            if(star1.isChecked() && star2.isChecked() && star3.isChecked() &&
                    star4.isChecked() && star5.isChecked()) rating =5;
            else if(star1.isChecked() && star2.isChecked() &&
                    star3.isChecked() && star4.isChecked()) rating =4;
            else if(star1.isChecked() && star2.isChecked() && star3.isChecked()) rating =3;
            else if(star1.isChecked() && star2.isChecked()) rating =2;
            else if(star1.isChecked()) rating =1;

            int finalRating = rating;
            Reviewers postReview = new Reviewers(users.getUid(), transactions.getDate(),
                    content.getText().toString(), valueOf(finalRating), null);

            repository.updateReview(id, postReview);
        });

        builder.show();
    }

    private void getSelected(CheckedTextView star1, CheckedTextView star2,
                             CheckedTextView star3, CheckedTextView star4,
                             CheckedTextView star5, boolean isChecked1, boolean isChecked2,
                             boolean isChecked3, boolean isChecked4, boolean isChecked5) {
        star1.setChecked(isChecked1);
        star2.setChecked(isChecked2);
        star3.setChecked(isChecked3);
        star4.setChecked(isChecked4);
        star5.setChecked(isChecked5);
    }

    private void onListener(TicketsAdapter productsAdapter,
                            ArrayList<TransactionsReference> ticket, Users users) {
        productsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
               startActivity(new Intent(getContext(), TicketDetailsActivity.class)
                       .putExtra("transaction", ticket.get(position))
                       .putExtra("user", users));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }
}