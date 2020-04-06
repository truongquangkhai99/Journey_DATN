package com.example.journey_datn.Model;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@androidx.room.Entity
public class Diary {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String date, title, contain;
    private int userId;

    public Diary() {
    }

    @Ignore
    public Diary(int id, String date, String title, String contain, int userId) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.contain = contain;
        this.userId = userId;
    }

    @Ignore
    public Diary(String date, String title, String contain, int userId) {
        this.date = date;
        this.title = title;
        this.contain = contain;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContain() {
        return contain;
    }

    public void setContain(String contain) {
        this.contain = contain;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
