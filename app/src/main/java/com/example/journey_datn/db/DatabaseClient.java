package com.example.journey_datn.db;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private Context mCtx;
    private static DatabaseClient mInstance;
    public static final String DATABASE_NAME = "Journey911";

    private EntityDatabase entityDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        entityDatabase = Room.databaseBuilder(mCtx, EntityDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public EntityDatabase getAppDatabase() {
        return entityDatabase;
    }

}
