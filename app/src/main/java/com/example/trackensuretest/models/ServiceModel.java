package com.example.trackensuretest.models;

import java.util.ArrayList;
import java.util.List;

public class ServiceModel {
    private static ServiceModel instance;
    private List<Refuel> refuelsToSync = new ArrayList<>();
    private List<Refuel> refuelsToRemove = new ArrayList<>();
    private List<GasStation> gasStationsToSync = new ArrayList<>();
    private boolean isConnected;

    private ServiceModel(){
    }

    public static ServiceModel getInstance(){
        if (instance == null){
            instance = new ServiceModel();
        }
        return instance;
    }

    public List<Refuel> getRefuelsToSync() {
        return refuelsToSync;
    }

    public void setRefuelsToSync(List<Refuel> refuelsToSync) {
        this.refuelsToSync = refuelsToSync;
    }

    public List<Refuel> getRefuelsToRemove() {
        return refuelsToRemove;
    }

    public void setRefuelsToRemove(List<Refuel> refuelsToRemove) {
        this.refuelsToRemove = refuelsToRemove;
    }

    public List<GasStation> getGasStationsToSync() {
        return gasStationsToSync;
    }

    public void setGasStationsToSync(List<GasStation> gasStationsToSync) {
        this.gasStationsToSync = gasStationsToSync;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
