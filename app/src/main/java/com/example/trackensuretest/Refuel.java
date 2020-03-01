package com.example.trackensuretest;

public class Refuel {

    private String fuelSupplierName;
    private FuelType fuelType;
    private double amount;
    private double price;
    private GasStation gasStation;

    public Refuel (String fuelSupplierName, FuelType fuelType, double amount, double price, GasStation gasStation){
        this.fuelSupplierName = fuelSupplierName;
        this.fuelType = fuelType;
        this.amount = amount;
        this.price = price;
        this.gasStation = gasStation;
    }

    public String getFuelSupplierName() {
        return fuelSupplierName;
    }

    public void setFuelSupplierName(String fuelSupplierName) {
        this.fuelSupplierName = fuelSupplierName;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
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

    public GasStation getGasStation() {
        return gasStation;
    }

    public void setGasStation(GasStation gasStation) {
        this.gasStation = gasStation;
    }
}
