package com.hactiv8.mytiket.payment;

import static com.hactiv8.mytiket.databinding.ActivityCreditCardVerificationBinding.inflate;
import static com.hactiv8.mytiket.util.LocalPreference.getInstance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hactiv8.mytiket.MainActivity;
import com.hactiv8.mytiket.R;
import com.hactiv8.mytiket.databinding.ActivityCreditCardVerificationBinding;
import com.hactiv8.mytiket.pojo.ScheduleReference;
import com.hactiv8.mytiket.pojo.Transactions;

public class CreditCardVerificationActivity extends AppCompatActivity {
    private Transactions transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_verification);

        ActivityCreditCardVerificationBinding binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        transactions = getIntent().getParcelableExtra("transactions");

        binding.layoutTotalPayment.tvTotal.setText(transactions.getTotalPayment());
        binding.appbar.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btnVerifyPayment.setOnClickListener(v -> onPushData());
    }

    private void onPushData() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Verify payment..");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = db.collection("transactions").document().getId();
        ScheduleReference schedule = getIntent().getParcelableExtra("schedule");

        transactions.setId(id);
        transactions.setPaymentMethod("credit card");
        transactions.setPaymentPartner("visa");

        db.collection("seats").document(schedule.getId())
                .set(schedule.getBuses().getSeats())
                .addOnCompleteListener(updateTask ->{
                    if(updateTask.isSuccessful()){
                        onDataChanged(dialog, db, id, schedule);
                    }
                });
    }

    private void onDataChanged(ProgressDialog dialog, FirebaseFirestore db,
                               String id, ScheduleReference schedule) {
        db.collection("transactions")
                .document(id).set(transactions)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        dialog.dismiss();
                        getInstance(this).getEditor().putBoolean("isRating", true).apply();
                        startActivity(new Intent(this, MainActivity.class)
                                .putExtra("schedule", schedule)
                                .putExtra("transactions", transactions)
                                .putExtra("fragment", 1));
                        finish();
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
}