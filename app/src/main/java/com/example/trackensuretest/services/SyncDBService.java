package com.example.trackensuretest.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.trackensuretest.Constants;
import com.example.trackensuretest.models.ServiceModel;
import com.example.trackensuretest.database.GasStationRepository;
import com.example.trackensuretest.database.RefuelRepository;
import com.example.trackensuretest.models.GasStation;
import com.example.trackensuretest.models.Refuel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SyncDBService extends IntentService {
    private static final String TAG = "SyncDBService";
    private GasStationRepository gasStationRepository;
    private RefuelRepository refuelRepository;
    private DatabaseReference databaseReference;
    private static final String FIREBASE_GAS_STATION = "gasStations";
    private static final String FIREBASE_REFUELS = "refuels";
    private ServiceModel model;

    public SyncDBService() {
        super(TAG);
        gasStationRepository = new GasStationRepository();
        refuelRepository = new RefuelRepository();
        model = ServiceModel.getInstance();
    }

    private boolean isAuth() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (!getUserId().isEmpty()){
            databaseReference = database.getReference(getUserId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras.containsKey(Constants.IS_NETWORK_CONNECTED_KEY)){
            boolean isConnected = extras.getBoolean(Constants.IS_NETWORK_CONNECTED_KEY);
            model.setConnected(isConnected);
        }
        if (extras.containsKey(Constants.REFUELS_KEY)) {
            List<Refuel> refuelsToSync = extras.getParcelableArrayList(Constants.REFUELS_KEY);
            model.setRefuelsToSync(refuelsToSync);
        }
        if (extras.containsKey(Constants.GAS_STATIONS_KEY)) {
            List<GasStation> gasStationsToSync = extras.getParcelableArrayList(Constants.GAS_STATIONS_KEY);
            model.setGasStationsToSync(gasStationsToSync);
        }
        if (extras.containsKey(Constants.DELETED_REFUELS_KEY)) {
            List<Refuel> refuelsToRemove = extras.getParcelableArrayList(Constants.DELETED_REFUELS_KEY);
            model.setRefuelsToRemove(refuelsToRemove);
        }
        if (isAuth() && model.isConnected()){
            syncRefuels(model.getRefuelsToSync());
            syncGasStations(model.getGasStationsToSync());
            removeRefuels(model.getRefuelsToRemove());
        }
    }

    private void removeRefuels(List<Refuel> refuelList){
        for (int i = 0; i < refuelList.size(); i++) {
            removeFromFirebase(FIREBASE_REFUELS, refuelList.get(i));
        }
    }

    private void syncRefuels(List<Refuel> refuels){
        for (int i = 0; i < refuels.size(); i++) {
            final Refuel refuel = refuels.get(i);
            databaseReference.child(FIREBASE_REFUELS).child(String.valueOf(refuel.getId())).setValue(refuel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            refuel.setSynced(true);
                            refuelRepository.updateTask(refuel);
                            model.getRefuelsToSync().remove(refuel);
                        }
                    });
        }
    }

    private void syncGasStations(List<GasStation> gasStations){
        for (int i = 0; i < gasStations.size(); i++) {
            final GasStation gasStation = gasStations.get(i);
            databaseReference.child(FIREBASE_GAS_STATION).child(String.valueOf(gasStation.getId())).setValue(gasStation)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            gasStation.setSynced(true);
                            gasStationRepository.updateTask(gasStation);
                            model.getGasStationsToSync().remove(gasStation);
                        }
                    });
        }
    }

    private String getUserId() {
        String userId = "";
        try {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (Exception ignored){ }
        return userId;
    }

    private void removeFromFirebase(String child, final Refuel refuel){
        Query applesQuery = databaseReference.child(child).orderByChild("id").equalTo(refuel.getId());
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    model.getRefuelsToRemove().remove(refuel);
                    refuelRepository.deleteTask(refuel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
