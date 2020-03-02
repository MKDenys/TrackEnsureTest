package com.example.trackensuretest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GasStationDao {
    @Query("SELECT * FROM GasStation")
    List<GasStation> getAll();

    @Query("SELECT MAX(id) FROM GasStation")
    int getMaxId();

    @Insert
    void insert(GasStation gasStation);

    @Update
    void update(GasStation gasStation);

    @Delete
    void delete(GasStation gasStation);

}
