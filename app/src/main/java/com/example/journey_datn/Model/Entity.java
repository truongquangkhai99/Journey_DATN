package com.example.journey_datn.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.RadioButton;

import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

public class Entity implements Parcelable {

    private int temperature, action, mood, star, day, month, year;
    private String id, srcImage, th, content, strPosition, strDate, textStyle;
    private double lat, lng;
    private boolean checkRdb = false;

    public Entity(String content, String textStyle, int action, String strPosition, int temperature,String strDate, int day, int month, int year, String th, int mood, int star, String srcImage, double lat, double lng) {
        this.content = content;
        this.textStyle = textStyle;
        this.action = action;
        this.strPosition = strPosition;
        this.temperature = temperature;
        this.strDate = strDate;
        this.day = day;
        this.month = month;
        this.year = year;
        this.th = th;
        this.mood = mood;
        this.star = star;
        this.srcImage = srcImage;
        this.lat = lat;
        this.lng = lng;
    }

    public Entity(String id, String content, String textStyle, int action, String strPosition, int temperature, String strDate, int day, int month, int year, String th, int mood, int star, String srcImage, double lat, double lng) {
        this.id = id;
        this.content = content;
        this.textStyle = textStyle;
        this.strPosition = strPosition;
        this.temperature = temperature;
        this.strDate = strDate;
        this.day = day;
        this.year = year;
        this.month = month;
        this.action = action;
        this.mood = mood;
        this.star = star;
        this.th = th;
        this.srcImage = srcImage;
        this.lat = lat;
        this.lng = lng;
    }

    public Entity(){

    }

    protected Entity(Parcel in) {
        id = in.readString();
        content = in.readString();
        textStyle = in.readString();
        strPosition = in.readString();
        temperature = in.readInt();
        strDate = in.readString();
        day = in.readInt();
        month = in.readInt();
        year = in.readInt();
        action = in.readInt();
        mood = in.readInt();
        star = in.readInt();
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
        dest.writeString(id);
        dest.writeString(content);
        dest.writeString(textStyle);
        dest.writeString(strPosition);
        dest.writeInt(temperature);
        dest.writeString(strDate);
        dest.writeInt(day);
        dest.writeInt(month);
        dest.writeInt(year);
        dest.writeInt(action);
        dest.writeInt(mood);
        dest.writeInt(star);
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(String textStyle) {
        this.textStyle = textStyle;
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

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTh() {
        return th;
    }

    public void setTh(String th) {
        this.th = th;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
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

    public boolean isCheckRdb() {
        return checkRdb;
    }

    public void setCheckRdb(boolean checkRdb) {
        this.checkRdb = checkRdb;
    }

}
