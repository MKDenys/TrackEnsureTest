package com.example.trackensuretest;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GasStation {

    @PrimaryKey
    private int id;
    private String name;
    private String address;

    public GasStation(String name, String address) {
        AppDatabase db = App.getInstance().getAppDatabase();
        this.id = db.gasStationDao().getMaxId() + 1;
        this.name = name;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
