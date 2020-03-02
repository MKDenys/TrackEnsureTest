package com.example.trackensuretest;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {GasStation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GasStationDao gasStationDao();
}
