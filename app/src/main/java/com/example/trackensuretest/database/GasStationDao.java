package com.example.trackensuretest.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.trackensuretest.models.GasStation;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface GasStationDao {
    @Query("SELECT * FROM GasStation")
    LiveData<List<GasStation>> getAll();

    @Query("SELECT * FROM GasStation WHERE synced = 0")
    LiveData<List<GasStation>> getNotSynced();

    @Query("DELETE FROM GasStation")
    void deleteAll();

    @Query("SELECT * FROM GasStation WHERE name = :name")
    LiveData<GasStation> getByName(String name);

    @Query("SELECT * FROM GasStation WHERE id = :id")
    LiveData<GasStation> getById(long id);

    @Insert(onConflict = REPLACE)
    void insert(GasStation... gasStations);

    @Update
    void update(GasStation... gasStations);

    @Delete
    void delete(GasStation... gasStations);
}
