package com.example.journey_datn.Model;

public class User {
    private String srcAvatar;
    private String username;
    private int dayUser, monthUser, yearUser;

    public User(String srcAvatar, String username) {
        this.srcAvatar = srcAvatar;
        this.username = username;
    }

    public User(int dayUser, int monthUser, int yearUser) {
        this.dayUser = dayUser;
        this.monthUser = monthUser;
        this.yearUser = yearUser;
    }

    public User(String srcAvatar, String username, int dayUser, int monthUser, int yearUser) {
        this.srcAvatar = srcAvatar;
        this.username = username;
        this.dayUser = dayUser;
        this.monthUser = monthUser;
        this.yearUser = yearUser;
    }

    public String getSrcAvatar() {
        return srcAvatar;
    }

    public void setSrcAvatar(String srcAvatar) {
        this.srcAvatar = srcAvatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDayUser() {
        return dayUser;
    }

    public void setDayUser(int dayUser) {
        this.dayUser = dayUser;
    }

    public int getMonthUser() {
        return monthUser;
    }

    public void setMonthUser(int monthUser) {
        this.monthUser = monthUser;
    }

    public int getYearUser() {
        return yearUser;
    }

    public void setYearUser(int yearUser) {
        this.yearUser = yearUser;
    }
}
