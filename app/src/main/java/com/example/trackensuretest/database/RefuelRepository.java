package com.example.trackensuretest.database;

import androidx.lifecycle.LiveData;

import com.example.trackensuretest.App;
import com.example.trackensuretest.database.async.DeleteRefuelAsyncTask;
import com.example.trackensuretest.database.async.InsertRefuelAsyncTask;
import com.example.trackensuretest.database.async.UpdateRefuelAsyncTask;
import com.example.trackensuretest.models.Refuel;

import java.util.List;

public class RefuelRepository {
    private RefuelDao refuelDao;

    public RefuelRepository() {
        refuelDao = App.getInstance().getAppDatabase().refuelDao();
    }

    public void insertTask(Refuel refuel){
        new InsertRefuelAsyncTask(refuelDao).execute(refuel);
    }

    public void updateTask(Refuel refuel){
        new UpdateRefuelAsyncTask(refuelDao).execute(refuel);
    }

    public void deleteTask(Refuel refuel){
        new DeleteRefuelAsyncTask(refuelDao).execute(refuel);
    }

    public LiveData<List<Refuel>> getAllTask() {
        return refuelDao.getAll();
    }

    public LiveData<List<Refuel>> getNotSyncedTask() {
        return refuelDao.getNotSynced();
    }

    public LiveData<List<Refuel>> getDeletedTask() {
        return refuelDao.getDeleted();
    }
}
