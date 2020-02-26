package com.example.journey_datn.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.journey_datn.Adapter.SectionsPagerAdapter;
import com.example.journey_datn.R;
import com.example.journey_datn.fragment.fragment_Journey;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private BottomNavigationView navigationView;
    private ImageView img_menu, img_search, img_cloud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        fragmentList.add(new fragment_Journey());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_journey:
                        mViewPager.setCurrentItem(0);
                        return  true;
                    case  R.id.menu_calendar:
                        mViewPager.setCurrentItem(1);
                        return  true;
                    case  R.id.menu_media:
                        mViewPager.setCurrentItem(2);
                        return  true;
                    case  R.id.menu_atlas:
                        mViewPager.setCurrentItem(3);
                        return  true;
                    case  R.id.menu_today:
                        mViewPager.setCurrentItem(4);
                        return  true;
                }
                return false;
            }
        });
    }

    private void init(){
        mViewPager =  findViewById(R.id.viewPager_contain);
        navigationView = findViewById(R.id.navigation);
        img_menu = findViewById(R.id.img_menu);
        img_search = findViewById(R.id.img_search);
        img_cloud = findViewById(R.id.img_cloud);
    }
}
