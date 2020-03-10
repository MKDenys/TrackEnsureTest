package com.example.trackensuretest.database.async;

import android.os.AsyncTask;

import com.example.trackensuretest.database.RefuelDao;
import com.example.trackensuretest.models.Refuel;

public class UpdateRefuelAsyncTask extends AsyncTask<Refuel, Void, Void> {

    private RefuelDao refuelDao;

    public UpdateRefuelAsyncTask(RefuelDao refuelDao) {
        this.refuelDao = refuelDao;
    }

    @Override
    protected Void doInBackground(Refuel... refuels) {
        this.refuelDao.update(refuels);
        return null;
    }
}
