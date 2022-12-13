package com.hactiv8.mytiket.util;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hactiv8.mytiket.api.CitiesRepository;
import com.hactiv8.mytiket.api.ScheduleRepository;
import com.hactiv8.mytiket.api.TransactionsRepository;
import com.hactiv8.mytiket.api.UsersRepository;
import com.hactiv8.mytiket.pojo.Cities;
import com.hactiv8.mytiket.pojo.ScheduleReference;
import com.hactiv8.mytiket.pojo.TransactionsReference;
import com.hactiv8.mytiket.pojo.Users;

import java.util.ArrayList;
import java.util.Calendar;

public class MainViewModel extends AndroidViewModel {
    private final ScheduleRepository scheduleRepository;
    private final CitiesRepository citiesRepository;
    private final TransactionsRepository transactionsRepository;
    private final UsersRepository usersRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.citiesRepository = new CitiesRepository();
        this.scheduleRepository = new ScheduleRepository();
        this.transactionsRepository = new TransactionsRepository();
        this.usersRepository = new UsersRepository();
    }

    public MutableLiveData<ArrayList<Cities>> getCities() {
        return citiesRepository.getCities();
    }

    public MutableLiveData<ArrayList<Cities>> getCities(String field, String value) {
        return citiesRepository.getCities(field, value);
    }

    public MutableLiveData<ArrayList<TransactionsReference>> getTransactions(String value) {
        return transactionsRepository.getTransactions(value);
    }

    public MutableLiveData<ArrayList<ScheduleReference>> getSchedule(
            Cities departureCity, Cities arrivalCity, Calendar calendar,  Users user) {
        return scheduleRepository.getSchedule(departureCity, arrivalCity, calendar, user);
    }

    public MutableLiveData<Users> getUsers(String uid) {
        return usersRepository.getUsers(uid);
    }
}
