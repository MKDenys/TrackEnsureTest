package com.example.trackensuretest;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface RefuelDao {
    @Query("SELECT * FROM Refuel")
    LiveData<List<Refuel>> getAll();

    @Query("SELECT COUNT(*) FROM Refuel WHERE gasStationId = :gasStationId")
    int getCountRefuelsInGasStation(int gasStationId);

    @Query("DELETE FROM Refuel")
    void deleteAll();

    @Insert(onConflict = REPLACE)
    void insert(Refuel refuel);

    @Update
    void update(Refuel refuel);

    @Delete
    void delete(Refuel refuel);
}
