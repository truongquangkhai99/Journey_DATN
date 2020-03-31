package com.example.journey_datn.Model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.PrimaryKey;
@androidx.room.Entity

@SuppressLint("ParcelCreator")
public class ImageJounrey implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idJounrey;
    private  String srcImage;

    public ImageJounrey() {
    }

    public ImageJounrey(int id, int idJounrey, String srcImage) {
        this.id = id;
        this.idJounrey = idJounrey;
        this.srcImage = srcImage;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdJounrey() {
        return idJounrey;
    }

    public void setIdJounrey(int idJounrey) {
        this.idJounrey = idJounrey;
    }

    public String getSrcImage() {
        return srcImage;
    }

    public void setSrcImage(String srcImage) {
        this.srcImage = srcImage;
    }


    protected ImageJounrey(Parcel in) {
    }

    public static final Creator<ImageJounrey> CREATOR = new Creator<ImageJounrey>() {
        @Override
        public ImageJounrey createFromParcel(Parcel in) {
            return new ImageJounrey(in);
        }

        @Override
        public ImageJounrey[] newArray(int size) {
            return new ImageJounrey[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
