package com.example.trackensuretest;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Refuel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String fuelSupplierName;
    private String fuelType;
    private double amount;
    private double price;
    private int gasStationId;

    public Refuel (String fuelSupplierName, String fuelType, double amount, double price, int gasStationId){
        this.fuelSupplierName = fuelSupplierName;
        this.fuelType = fuelType;
        this.amount = amount;
        this.price = price;
        this.gasStationId = gasStationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getGasStationId() {
        return gasStationId;
    }

    public void setGasStationId(int gasStationId) {
        this.gasStationId = gasStationId;
    }
}
