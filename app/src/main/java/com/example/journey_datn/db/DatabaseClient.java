package com.example.journey_datn.db;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private Context mCtx;
    private static DatabaseClient mInstance;
    public static final String DATABASE_NAME = "Journey11";

    private EntityDatabase entityDatabase;
    private ImageDatabase imageDatabase;



    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        entityDatabase = Room.databaseBuilder(mCtx, EntityDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        imageDatabase = Room.databaseBuilder(mCtx, ImageDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
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
    public ImageDatabase getImageDatabase() {
        return imageDatabase;
    }
}
