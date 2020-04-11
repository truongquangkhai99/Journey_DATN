package com.example.journey_datn.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.example.journey_datn.Adapter.AdapterRcvAllDiary;
import com.example.journey_datn.R;
import com.example.journey_datn.fragment.FragmentAllDiary;
import com.example.journey_datn.fragment.FragmentWriteDiary;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class DairyActivity extends AppCompatActivity implements FragmentWriteDiary.clickItemDiary {
    private ViewPager mViewPager;

    BottomNavigationView navigationView;
    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy);
        mViewPager = findViewById(R.id.viewPager);
        navigationView = findViewById(R.id.navigationd);
        fragmentList.add(new FragmentWriteDiary());
        fragmentList.add(new FragmentAllDiary());
        ((FragmentWriteDiary) fragmentList.get(0)).setOnClickDiary(this);

        ((FragmentAllDiary) fragmentList.get(1)).setOnClickItemTab1(((FragmentWriteDiary) fragmentList.get(0)).getOnClickItemTab1());

        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentList));

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_write_diary:
                        mViewPager.setCurrentItem(0);
                        return true;
                    case R.id.menu_all_diary:
                        hideKeyboard(DairyActivity.this);
                        mViewPager.setCurrentItem(1);
                        return true;
                }
                return false;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    navigationView.getMenu().getItem(0).setChecked(true);
                } else {
                    hideKeyboard(DairyActivity.this);
                    navigationView.getMenu().getItem(1).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClickDiary(boolean isClick) {
        mViewPager.setCurrentItem(0);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList;

        public ViewPagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragmentList = fragments;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return fragmentList.get(0);
                case 1:
                    return fragmentList.get(1);
                default:
                    return fragmentList.get(0);
            }
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }
    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
