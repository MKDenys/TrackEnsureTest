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

    @Query("DELETE FROM GasStation")
    void deleteAll();

    @Query("SELECT * FROM GasStation WHERE name = :name")
    GasStation getByName(String name);

    @Query("SELECT * FROM GasStation WHERE id = :id")
    GasStation getById(int id);

    @Insert
    void insert(GasStation gasStation);

    @Update
    void update(GasStation gasStation);

    @Delete
    void delete(GasStation gasStation);
}
