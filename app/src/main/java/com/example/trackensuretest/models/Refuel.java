package com.example.trackensuretest.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
@Entity
public class Refuel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String fuelSupplierName;
    private String fuelType;
    private double amount;
    private double price;
    private long gasStationId;
    private boolean synced;
    private boolean deleted;

    public Refuel (String fuelSupplierName, String fuelType, double amount, double price, long gasStationId){
        this.fuelSupplierName = fuelSupplierName;
        this.fuelType = fuelType;
        this.amount = amount;
        this.price = price;
        this.gasStationId = gasStationId;
        this.synced = false;
        this.deleted = false;
    }

    public Refuel() {
    }

    protected Refuel(Parcel in) {
        id = in.readLong();
        fuelSupplierName = in.readString();
        fuelType = in.readString();
        amount = in.readDouble();
        price = in.readDouble();
        gasStationId = in.readLong();
        synced = in.readByte() != 0;
        deleted = in.readByte() != 0;
    }

    public static final Creator<Refuel> CREATOR = new Creator<Refuel>() {
        @Override
        public Refuel createFromParcel(Parcel in) {
            return new Refuel(in);
        }

        @Override
        public Refuel[] newArray(int size) {
            return new Refuel[size];
        }
    };

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

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(fuelSupplierName);
        dest.writeString(fuelType);
        dest.writeDouble(amount);
        dest.writeDouble(price);
        dest.writeLong(gasStationId);
        dest.writeByte((byte) (synced ? 1 : 0));
        dest.writeByte((byte) (deleted ? 1 : 0));
    }
}
