package com.example.trackensuretest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RefuelDao {
    @Query("SELECT * FROM Refuel")
    List<Refuel> getAll();

    @Query("DELETE FROM Refuel")
    void deleteAll();

    @Insert
    void insert(Refuel refuel);

    @Update
    void update(Refuel refuel);

    @Delete
    void delete(Refuel refuel);
}
