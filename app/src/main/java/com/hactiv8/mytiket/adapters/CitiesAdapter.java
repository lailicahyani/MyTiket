package com.hactiv8.mytiket.adapters;



import static com.hactiv8.mytiket.util.Constant.toUpperCase;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hactiv8.mytiket.interfaces.ItemClickListener;
import com.hactiv8.mytiket.databinding.ItemDestinationSearchBinding;
import com.hactiv8.mytiket.pojo.Cities;

import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ItemClickListener itemClickListener;
    private final List<Cities> citiesList;

    public CitiesAdapter(List<Cities> citiesList) {
        this.citiesList = citiesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDestinationSearchBinding binding = ItemDestinationSearchBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ScheduleViewHolder(binding, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ScheduleViewHolder scheduleViewHolder = (ScheduleViewHolder) holder;
        scheduleViewHolder.setDataToView(citiesList.get(position));
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private final ItemDestinationSearchBinding binding;
        private final CitiesAdapter scheduleAdapter;

        public ScheduleViewHolder(ItemDestinationSearchBinding binding, CitiesAdapter scheduleAdapter) {
            super(binding.getRoot());
            this.binding = binding;
            this.scheduleAdapter = scheduleAdapter;
        }

        @SuppressLint("SetTextI18n")
        public void setDataToView(Cities citiesList) {
            binding.tvCity.setText(toUpperCase(citiesList.getCity()));
            binding.tvTerminal.setText(toUpperCase(citiesList.getTerminal()));

            binding.getRoot().setOnLongClickListener(this);
            binding.getRoot().setOnClickListener(this);
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

