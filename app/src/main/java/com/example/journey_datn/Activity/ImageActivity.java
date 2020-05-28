package com.example.journey_datn.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.journey_datn.Adapter.AdapterViewPagerImage;
import com.example.journey_datn.R;
import com.example.journey_datn.fragment.FragmentImage;

public class ImageActivity extends AppCompatActivity {

    private AdapterViewPagerImage adapterViewPagerImage;
    private ViewPager mViewPager;
    private String[] srcImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mViewPager = findViewById(R.id.viewpager_image);
        getData();
        adapterViewPagerImage = new AdapterViewPagerImage(getSupportFragmentManager(), srcImg);
        mViewPager.setAdapter(adapterViewPagerImage);

    }

    /**
     * lấy thông tin từ FragmentDetail truyền sang thông qua intent
     */
    private void getData(){
        Intent intent = getIntent();
        srcImg = intent.getStringArrayExtra("srcImg");
    }
}
