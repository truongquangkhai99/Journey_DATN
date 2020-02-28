package com.example.journey_datn.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.journey_datn.Model.Entity;

@Database(entities = {Entity.class}, version = 1)
public abstract class EntityDatabase extends RoomDatabase{

    private static EntityDatabase INSTANCE;
    public static final String DATABASE_NAME = "RoomJourney";

    public abstract EntityDAO EntityDao();

    public static EntityDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
//            INSTANCE = inMemoryDatabaseBuilder(context.getApplicationContext(), EntityDatabase.class).allowMainThreadQueries().build();
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EntityDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
