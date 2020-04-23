package com.example.journey_datn.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

public class User  implements Parcelable {
    private String idUser;
    private String firstName, lastName, username;


    public User(String id, String firstName, String lastName, String username) {
        this.idUser = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public User(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public User() {
    }

    protected User(Parcel in) {
        idUser = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        username = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return idUser;
    }

    public void setId(String id) {
        this.idUser = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idUser);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(username);
    }
}
