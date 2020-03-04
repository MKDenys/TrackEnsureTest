package com.example.trackensuretest;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
@Entity
public class Refuel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String fuelSupplierName;
    private String fuelType;
    private double amount;
    private double price;
    private long gasStationId;

    public Refuel (long id, String fuelSupplierName, String fuelType, double amount, double price, long gasStationId){
        this.id = id;
        this.fuelSupplierName = fuelSupplierName;
        this.fuelType = fuelType;
        this.amount = amount;
        this.price = price;
        this.gasStationId = gasStationId;
    }

    public Refuel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFuelSupplierName() {
        return fuelSupplierName;
    }

    public void setFuelSupplierName(String fuelSupplierName) {
        this.fuelSupplierName = fuelSupplierName;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCost() {
        return price * amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getGasStationId() {
        return gasStationId;
    }

    public void setGasStationId(long gasStationId) {
        this.gasStationId = gasStationId;
    }
}
