package com.example.journey_datn.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;


import com.example.journey_datn.Adapter.AdapterRcvAllDiary;
import com.example.journey_datn.R;
import com.example.journey_datn.fragment.FragmentAllDiary;
import com.example.journey_datn.fragment.FragmentWriteDiary;
import com.google.android.material.tabs.TabLayout;


public class DairyActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    Fragment fragmentWriteDiary;
    Fragment fragmentAllDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy);
        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tabLayout);
        fragmentWriteDiary = new FragmentWriteDiary();
        fragmentAllDiary = new FragmentAllDiary();
        ((FragmentAllDiary) fragmentAllDiary).setOnClickItemTab1(((FragmentWriteDiary) fragmentWriteDiary).getOnClickItemTab1());

        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public  class ViewPagerAdapter extends FragmentPagerAdapter{

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return fragmentWriteDiary;
                case 1:
                    return fragmentAllDiary;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Write diary";
                case 1:
                    return "All diary";
            }
            return super.getPageTitle(position);
        }
    }
}
