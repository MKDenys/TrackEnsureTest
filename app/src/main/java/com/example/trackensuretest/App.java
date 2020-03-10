package com.example.trackensuretest;

import android.app.Application;

import androidx.room.Room;

import com.example.trackensuretest.database.AppDatabase;

public class App extends Application {

    public static App instance;

    private AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "database")
                .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3,
                        AppDatabase.MIGRATION_3_4, AppDatabase.MIGRATION_4_5, AppDatabase.MIGRATION_5_6,
                        AppDatabase.MIGRATION_6_7, AppDatabase.MIGRATION_7_8, AppDatabase.MIGRATION_8_9,
                        AppDatabase.MIGRATION_9_10)
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}