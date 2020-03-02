package com.example.journey_datn.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class AdapterPagerDetail extends FragmentPagerAdapter {
    public List<Fragment> fragmentList;
    public AdapterPagerDetail(@NonNull FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragmentList = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(0);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
