package com.example.journey_datn.Model;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

public class Diary {
    private String id;
    private String date, title, contain;
    private boolean checkRdb = false;

    public Diary() {
    }

    public Diary(String id, String date, String title, String contain) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.contain = contain;

    }

    public Diary(String date, String title, String contain) {
        this.date = date;
        this.title = title;
        this.contain = contain;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public boolean isCheckRdb() {
        return checkRdb;
    }

    public void setCheckRdb(boolean checkRdb) {
        this.checkRdb = checkRdb;
    }
}
