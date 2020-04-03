package com.example.journey_datn.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.Model.User;

import java.util.List;

@Dao
public interface EntityDAO {
    @Insert
    void insertEntity(Entity entity);

    @Update
    void updateEntity(Entity entity);

    @Query("DELETE FROM Entity")
    void deleteAll();

    @Query("SELECT * FROM Entity WHERE userId = :uId")
    List<Entity> getAllEntity(int uId);

    @Query("SELECT * FROM Entity WHERE id = :entityID AND userId = :uId")
    Entity getEntityById(int entityID, int uId);

    @Query("SELECT * FROM Entity WHERE content LIKE :content AND userId = :uId")
    List<Entity> getEntityByContent(String content, int uId);

    @Query("SELECT * FROM Entity WHERE day = :day AND month = :month AND year = :year AND userId = :uId")
    List<Entity> getEntityByTime(int day, int month, int year, int uId);

    @Delete
    void deleteEntity(Entity entity);

    @Query("SELECT COUNT(*) FROM Entity WHERE userId = :uId")
    int getCountItem(int uId);
}
