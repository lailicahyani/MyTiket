package com.hactiv8.mytiket.adapters;

import static com.hactiv8.mytiket.util.Constant.getTime;
import static com.hactiv8.mytiket.util.Constant.toUpperCase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hactiv8.mytiket.databinding.ItemOrderBinding;
import com.hactiv8.mytiket.interfaces.ItemClickListener;
import com.hactiv8.mytiket.pojo.Buses;
import com.hactiv8.mytiket.pojo.Cities;
import com.hactiv8.mytiket.pojo.ScheduleReference;
import com.hactiv8.mytiket.pojo.Transactions;
import com.hactiv8.mytiket.pojo.TransactionsReference;

import java.util.ArrayList;
import java.util.List;

public class TicketsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ItemClickListener itemClickListener;
    private final List<TransactionsReference> transactionsList;

    public TicketsAdapter(ArrayList<TransactionsReference> transactionsList) {
        this.transactionsList = transactionsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ScheduleViewHolder(binding, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ScheduleViewHolder scheduleViewHolder = (ScheduleViewHolder) holder;
        scheduleViewHolder.setDataToView(transactionsList.get(position));
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private final ItemOrderBinding binding;
        private final TicketsAdapter scheduleAdapter;
        private final FirebaseFirestore db;

        public ScheduleViewHolder(ItemOrderBinding binding, TicketsAdapter scheduleAdapter) {
            super(binding.getRoot());
            this.binding = binding;
            this.scheduleAdapter = scheduleAdapter;
            this.db = FirebaseFirestore.getInstance();
        }

        public void setDataToView(TransactionsReference transactionsReference) {
            Transactions transactions = transactionsReference.getTransactions();
            ScheduleReference scheduleReference = transactionsReference.getReference();
            Buses buses = scheduleReference.getBuses();
            Cities departureCity = scheduleReference.getDeparture();
            String bookNo = "Book No. "+transactionsReference.getTransactions().getBookNo();

            binding.tvDate.setText(transactions.getDate());
            binding.tvBookNo.setText(bookNo);
            binding.tvStatus.setText(toUpperCase(transactions.getStatus()));
            binding.tvPOName.setText(toUpperCase(buses.getPoName()));
            binding.tvBusNo.setText(buses.getBusNo());
            binding.tvDeparture.setText(toUpperCase(departureCity.getCity()));
            binding.tvTerminalDeparture.setText(toUpperCase(departureCity.getTerminal()));
            binding.tvDepartureTime.setText(getTime(scheduleReference).get("departureTime"));

            binding.getRoot().setOnClickListener(this);
            binding.getRoot().setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(scheduleAdapter.itemClickListener != null){
                scheduleAdapter.itemClickListener.onClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(scheduleAdapter.itemClickListener != null){
                scheduleAdapter.itemClickListener.onLongClick(v, getAdapterPosition());
            }
            return true;
        }
    }
}