package com.example.journey_datn.Model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@androidx.room.Entity
public class Entity implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int temperature, action, mood, year, month, day, hour, minute;
    private String srcImage, th, content, strPosition;
    private double lat, lng;

    @Ignore
    public Entity(String content, int action, String strPosition, int temperature, int year, int month, int day, String th, int hour, int minute, int mood, String srcImage, double lat, double lng) {
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
        this.lat = lat;
        this.lng = lng;
    }

    @Ignore
    public Entity(int id, String content, int action, String strPosition, int temperature, int year, int month, int day, String th,  int hour, int minute,  int mood, String srcImage, double lat, double lng) {
        this.id = id;
        this.content = content;
        this.strPosition = strPosition;
        this.temperature = temperature;
        this.action = action;
        this.mood = mood;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.th = th;
        this.srcImage = srcImage;
        this.lat = lat;
        this.lng = lng;
    }

    public Entity(){

    }

    protected Entity(Parcel in) {
        id = in.readInt();
        content = in.readString();
        strPosition = in.readString();
        temperature = in.readInt();
        action = in.readInt();
        mood = in.readInt();
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        th = in.readString();
        srcImage = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(content);
        dest.writeString(strPosition);
        dest.writeInt(temperature);
        dest.writeInt(action);
        dest.writeInt(mood);
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeString(th);
        dest.writeString(srcImage);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    public static final Creator<Entity> CREATOR = new Creator<Entity>() {
        @Override
        public Entity createFromParcel(Parcel in) {
            return new Entity(in);
        }

        @Override
        public Entity[] newArray(int size) {
            return new Entity[size];
        }
    };


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

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
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

    public String getTh() {
        return th;
    }

    public void setTh(String th) {
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

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public String getSrcImage() {
        return srcImage;
    }

    public void setSrcImage(String srcImage) {
        this.srcImage = srcImage;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
