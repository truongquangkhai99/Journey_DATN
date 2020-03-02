package com.example.journey_datn.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.journey_datn.R;

public class FragmentItemDetail extends Fragment {

    private ImageView img_item_detail, img_action_detail,img_mood_detail;
    private TextView  txt_day_detail,txt_month_detail,txt_year_detail,txt_th_detail,
            txt_hour_detail,txt_minute_detail,txt_position_detail,txt_temperature_detail,txt_contain_detail;

    private String content,srcImage,strPosition, th;
    private int year, month, day, hour, minute,temperature, action, mood;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_item, container, false);
        initView(view);
        setData();
        return view;
    }

    private void initView(View view){
        img_item_detail = view.findViewById(R.id.img_item_detail);
        img_action_detail = view.findViewById(R.id.img_action_detail);
        img_mood_detail = view.findViewById(R.id.img_mood_detail);
        txt_day_detail = view.findViewById(R.id.txt_day_detail);
        txt_month_detail = view.findViewById(R.id.txt_month_detail);
        txt_year_detail = view.findViewById(R.id.txt_year_detail);
        txt_th_detail = view.findViewById(R.id.txt_th_detail);
        txt_hour_detail = view.findViewById(R.id.txt_hour_detail);
        txt_minute_detail = view.findViewById(R.id.txt_minute_detail);
        txt_position_detail = view.findViewById(R.id.txt_position_detail);
        txt_temperature_detail = view.findViewById(R.id.txt_temperature_detail);
        txt_contain_detail = view.findViewById(R.id.txt_contain_detail);
    }

    private void setData(){

        Glide.with(getContext()).load(getMood()).into(img_mood_detail);
        Glide.with(getContext()).load(getAction()).into(img_action_detail);
        Glide.with(getContext()).load(Uri.parse(getSrcImage())).into(img_item_detail);


        txt_day_detail.setText(getDay()+ "");
        txt_month_detail.setText(getMonth()+ "");
        txt_year_detail.setText(getYear()+ "");
        txt_th_detail.setText(getTh()+ "");
        txt_hour_detail.setText(getHour()+ "");
        txt_minute_detail.setText(getMinute()+ "");
        txt_temperature_detail.setText(getTemperature()+ "");
        txt_position_detail.setText(getStrPosition());
        txt_contain_detail.setText(getContent());

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

    public String getStrPosition() {
        return strPosition;
    }

    public void setStrPosition(String strPosition) {
        this.strPosition = strPosition;
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

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
