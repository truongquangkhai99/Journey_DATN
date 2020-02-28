package com.example.journey_datn.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.journey_datn.Model.Entity;

import java.util.List;

import io.reactivex.Flowable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface EntityDAO {
    @Insert(onConflict = REPLACE)
    void insertEntity(Entity entity);

    @Update(onConflict = REPLACE)
    void updateEntity(Entity entity);

    @Query("DELETE FROM Entity")
    void deleteAll();

    @Query("SELECT * FROM Entity")
    Flowable<List<Entity>> getAllEntity();

    @Query("SELECT * FROM Entity WHERE id = :entityID")
    Entity getEntityById(int entityID);

    @Query("SELECT * FROM Entity WHERE content LIKE :content")
    Entity getEntityByContent(String content);

    @Delete
    void deleteEntity(Entity entity);
}
