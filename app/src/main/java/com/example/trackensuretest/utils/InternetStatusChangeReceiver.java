package com.example.trackensuretest.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.trackensuretest.Constants;
import com.example.trackensuretest.services.SyncDBService;

public class InternetStatusChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent.setClass(context, SyncDBService.class);
        intent.putExtra(Constants.IS_NETWORK_CONNECTED_KEY, isConnected(context));
        context.startService(intent);
    }

    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
