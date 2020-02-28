package com.example.journey_datn.Model;

import androidx.annotation.NonNull;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@androidx.room.Entity
public class Entity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;


    private String content, action;
    private String strPosition;
    private int temperature;
    private int year, month, day, th, hour, minute;
    private String mood;
    private String srcImage;

    @Ignore
    public Entity(String content, String action, String strPosition, int temperature, int year, int month, int day, int th, int hour, int minute, String mood, String srcImage) {
        this.content = content;
        this.action = action;
        this.strPosition = strPosition;
        this.temperature = temperature;
        this.year = year;
        this.month = month;
        this.day = day;
        this.th = th;
        this.hour = hour;
        this.minute = minute;
        this.mood = mood;
        this.srcImage = srcImage;
    }

    public Entity(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStrPosition() {
        return strPosition;
    }

    public void setStrPosition(String strPosition) {
        this.strPosition = strPosition;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTh() {
        return th;
    }

    public void setTh(int th) {
        this.th = th;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getSrcImage() {
        return srcImage;
    }

    public void setSrcImage(String srcImage) {
        this.srcImage = srcImage;
    }
}
