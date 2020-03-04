package com.example.journey_datn.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

import com.example.journey_datn.Adapter.AdapterPagerDetail;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;

import java.util.ArrayList;

public class ItemDetailActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private AdapterPagerDetail adapterPagerDetail;
    private ArrayList<Entity> lstEntity;
    private int position;
    private  Entity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        getDataIntent();
        mViewPager = findViewById(R.id.viewpager_detail);
        adapterPagerDetail = new AdapterPagerDetail(getSupportFragmentManager(), lstEntity);
        mViewPager.setAdapter(adapterPagerDetail);
        mViewPager.setCurrentItem(position);

    }

    public void getDataIntent(){
        Intent intent = getIntent();
        Entity entity = intent.getParcelableExtra("entity");
        lstEntity =  intent.getParcelableArrayListExtra("listEntity");
        position = intent.getIntExtra("position", 0);
        setEntity(entity);
    }

    public Entity getEntity(){
        return  entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
