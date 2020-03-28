package com.example.journey_datn.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.journey_datn.Model.Entity;

import java.util.List;

@Dao
public interface EntityDAO {
    @Insert
    void insertEntity(Entity entity);

    @Update
    void updateEntity(Entity entity);

    @Query("DELETE FROM Entity")
    void deleteAll();

    @Query("SELECT * FROM Entity")
    List<Entity> getAllEntity();

    @Query("SELECT * FROM Entity WHERE id = :entityID")
    Entity getEntityById(int entityID);

    @Query("SELECT * FROM Entity WHERE content LIKE :content")
    List<Entity> getEntityByContent(String content);

    @Query("SELECT * FROM Entity WHERE day = :day AND month = :month AND year = :year")
    List<Entity> getEntityByTime(int day, int month, int year);

    @Delete
    void deleteEntity(Entity entity);

    @Query("SELECT COUNT(*) FROM Entity")
    int getCountItem();
}
