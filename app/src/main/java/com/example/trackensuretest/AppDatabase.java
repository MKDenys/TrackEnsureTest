package com.example.trackensuretest;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {GasStation.class, Refuel.class}, version = 7)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GasStationDao gasStationDao();
    public abstract RefuelDao refuelDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {

        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE GasStation");
        }
    };

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE GasStation (" +
                    "id INTEGER DEFAULT 0 NOT NULL PRIMARY KEY," +
                    "name TEXT," +
                    "address TEXT," +
                    "latitude REAL NOT NULL," +
                    "longitude REAL NOT NULL)");
        }
    };

    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE GasStation");
        }
    };

    public static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE GasStation (" +
                    "id INTEGER DEFAULT 0 NOT NULL PRIMARY KEY," +
                    "name TEXT," +
                    "address TEXT," +
                    "latitude REAL NOT NULL," +
                    "longitude REAL NOT NULL)");
        }
    };

    public static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE Refuel (" +
                    "id INTEGER DEFAULT 0 NOT NULL PRIMARY KEY," +
                    "fuelSupplierName TEXT," +
                    "fuelType TEXT," +
                    "amount REAL NOT NULL," +
                    "price REAL NOT NULL," +
                    "gasStationId INTEGER NOT NULL)");
        }
    };
}
