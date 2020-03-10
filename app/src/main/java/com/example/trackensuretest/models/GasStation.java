package com.example.trackensuretest.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
@Entity
public class GasStation implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private boolean synced;

    public GasStation(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.synced = false;
    }

    protected GasStation(Parcel in) {
        id = in.readLong();
        name = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        synced = in.readByte() != 0;
    }

    public static final Creator<GasStation> CREATOR = new Creator<GasStation>() {
        @Override
        public GasStation createFromParcel(Parcel in) {
            return new GasStation(in);
        }

        @Override
        public GasStation[] newArray(int size) {
            return new GasStation[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (synced ? 1 : 0));
    }
}
