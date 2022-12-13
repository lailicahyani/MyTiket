package com.hactiv8.mytiket.api;

import static com.hactiv8.mytiket.util.Constant.COLLECTION_BUSES;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hactiv8.mytiket.pojo.Buses;

public class BusesRepository {
    private final CollectionReference collection;
    private final FirebaseFirestore db;

    public BusesRepository() {
        db = FirebaseFirestore.getInstance();
        collection = db.collection(COLLECTION_BUSES);
    }

    public MutableLiveData<Buses> getBuses(String reference) {
        MutableLiveData<Buses> listMutableLiveData = new MutableLiveData<>();
        db.document(reference)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Buses buses = task.getResult().toObject(Buses.class);
                listMutableLiveData.setValue(buses);
            } else listMutableLiveData.setValue(null);
        });
        return listMutableLiveData;
    }
}
