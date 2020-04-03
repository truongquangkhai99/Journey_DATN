package com.example.journey_datn.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.journey_datn.Model.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM User")
    List<User> getAllUser();
}
