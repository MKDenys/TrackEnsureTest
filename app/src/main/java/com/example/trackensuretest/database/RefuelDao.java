package com.example.trackensuretest.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.trackensuretest.models.Refuel;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface RefuelDao {
    @Query("SELECT * FROM Refuel WHERE deleted = 0")
    LiveData<List<Refuel>> getAll();

    @Query("SELECT * FROM Refuel WHERE synced = 0 AND deleted = 0")
    LiveData<List<Refuel>> getNotSynced();

    @Query("SELECT * FROM Refuel WHERE deleted = 1")
    LiveData<List<Refuel>> getDeleted();

    @Query("DELETE FROM Refuel")
    void deleteAll();

    @Insert(onConflict = REPLACE)
    void insert(Refuel... refuels);

    @Update
    void update(Refuel... refuels);

    @Delete
    void delete(Refuel... refuels);
}
