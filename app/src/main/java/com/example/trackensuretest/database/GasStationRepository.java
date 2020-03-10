package com.example.trackensuretest.database;

import androidx.lifecycle.LiveData;

import com.example.trackensuretest.App;
import com.example.trackensuretest.database.async.DeleteGasStationAsyncTask;
import com.example.trackensuretest.database.async.InsertGasStationAsyncTask;
import com.example.trackensuretest.database.async.UpdateGasStationAsyncTask;
import com.example.trackensuretest.models.GasStation;

import java.util.List;

public class GasStationRepository {
    private GasStationDao gasStationDao;

    public GasStationRepository() {
        gasStationDao = App.getInstance().getAppDatabase().gasStationDao();
    }

    public void insertTask(GasStation gasStation){
        new InsertGasStationAsyncTask(gasStationDao).execute(gasStation);
    }

    public void updateTask(GasStation gasStation){
        new UpdateGasStationAsyncTask(gasStationDao).execute(gasStation);
    }

    public void deleteTask(GasStation gasStation){
        new DeleteGasStationAsyncTask(gasStationDao).execute(gasStation);
    }

    public LiveData<List<GasStation>> getAllTask() {
        return gasStationDao.getAll();
    }

    public LiveData<List<GasStation>> getNotSyncedTask() {
        return gasStationDao.getNotSynced();
    }

    public LiveData<GasStation> getByNameTask(String gasStationName) {
        return gasStationDao.getByName(gasStationName);
    }

    public LiveData<GasStation> getByIdTask(long gasStationId) {
        return gasStationDao.getById(gasStationId);
    }
}
