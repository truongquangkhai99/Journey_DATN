package com.example.journey_datn.db;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.journey_datn.Model.ImageJounrey;

import java.util.List;

@Dao
public interface ImageDao {
    @Insert
    void insertEntity(ImageJounrey imageJounrey);

    @Update
    void updateEntity(ImageJounrey imageJounrey);

    @Query("DELETE FROM ImageJounrey")
    void deleteAll();

    @Query("SELECT * FROM ImageJounrey")
    List<ImageJounrey> getAllImage();

    @Query("SELECT COUNT(*) FROM ImageJounrey")
    Integer getCountItem();


}
