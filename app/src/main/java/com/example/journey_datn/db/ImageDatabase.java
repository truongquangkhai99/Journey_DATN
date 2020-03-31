package com.example.journey_datn.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.Model.ImageJounrey;

@Database(entities = {ImageJounrey.class}, version = 1,exportSchema = false)
public abstract class ImageDatabase extends RoomDatabase {
    public abstract ImageDao ImgaeDao();
}
