package com.example.trackensuretest.database.async;

import android.os.AsyncTask;

import com.example.trackensuretest.database.GasStationDao;
import com.example.trackensuretest.models.GasStation;

public class UpdateGasStationAsyncTask extends AsyncTask<GasStation, Void, Void> {

    private GasStationDao gasStationDao;

    public UpdateGasStationAsyncTask(GasStationDao gasStationDao) {
        this.gasStationDao = gasStationDao;
    }

    @Override
    protected Void doInBackground(GasStation... gasStations) {
        this.gasStationDao.update(gasStations);
        return null;
    }
}
