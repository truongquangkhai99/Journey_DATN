package com.example.journey_datn.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.Model.User;

@Database(entities = {Entity.class, User.class}, version = 1)
public abstract class EntityDatabase extends RoomDatabase{
    public abstract EntityDAO EntityDao();
    public abstract UserDAO UserDao();
}
