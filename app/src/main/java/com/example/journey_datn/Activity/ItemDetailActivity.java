package com.example.journey_datn.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.journey_datn.Adapter.AdapterPagerDetail;
import com.example.journey_datn.R;
import com.example.journey_datn.fragment.FragmentItemDetail;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailActivity extends AppCompatActivity {

    private List<Fragment> fragmentList = new ArrayList<>();
    private ViewPager mViewPager;
    private AdapterPagerDetail adapterPagerDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        mViewPager = findViewById(R.id.viewpager_detail);
        fragmentList.add(new FragmentItemDetail());
        adapterPagerDetail = new AdapterPagerDetail(getSupportFragmentManager(), fragmentList);
        setDataIntent();
        mViewPager.setAdapter(adapterPagerDetail);

    }

    private void setDataIntent(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");

        FragmentItemDetail mfragment = ((FragmentItemDetail)adapterPagerDetail.fragmentList.get(0));

        mfragment.setContent(bundle.getString("content"));
        mfragment.setAction(bundle.getInt("action"));
        mfragment.setStrPosition(bundle.getString("strPosition"));
        mfragment.setMood(bundle.getInt("mood"));
        mfragment.setSrcImage(bundle.getString("srcImage"));
        mfragment.setTemperature(bundle.getInt("temperature"));
        mfragment.setYear(bundle.getInt("year"));
        mfragment.setMonth(bundle.getInt("month"));
        mfragment.setDay(bundle.getInt("day"));
        mfragment.setTh(bundle.getString("th"));
        mfragment.setHour(bundle.getInt("hour"));
        mfragment.setMinute(bundle.getInt("minute"));
    }
}
