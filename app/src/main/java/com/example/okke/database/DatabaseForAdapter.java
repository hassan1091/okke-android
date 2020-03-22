package com.example.okke.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {LastUrlList.class}, version = 1, exportSchema = false)
public abstract class DatabaseForAdapter extends RoomDatabase {
    private static Object LACK = new Object();
    private static DatabaseForAdapter sInstance;
    private static final String DATABASE_NAME = "databaseForAdapter";

    public static DatabaseForAdapter getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LACK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(
                            context.getApplicationContext(), DatabaseForAdapter.class, DATABASE_NAME
                    ).build();
                }
            }
        }
        return sInstance;
    }

    public abstract LastUrlDaw lastUrlDaw();

}
