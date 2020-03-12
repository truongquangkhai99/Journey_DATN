package com.example.journey_datn.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.journey_datn.Model.Entity;

@Database(entities = {Entity.class}, version = 1)
public abstract class EntityDatabase extends RoomDatabase{
    public abstract EntityDAO EntityDao();
}
