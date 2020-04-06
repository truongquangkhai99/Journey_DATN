package com.example.journey_datn.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.journey_datn.Model.Diary;

import java.util.List;

@Dao
public interface DiaryDAO {

    @Insert
    void insertDiary(Diary diary);

    @Query("SELECT * FROM Diary WHERE userId = :uId")
    List<Diary> getAllDiary(int uId);

    @Update
    void updateDiary(Diary diary);

    @Delete
    void deleteDiary(Diary diary);

}
