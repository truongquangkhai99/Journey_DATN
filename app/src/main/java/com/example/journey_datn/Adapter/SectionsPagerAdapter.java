package com.example.journey_datn.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.List;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public List<Fragment> fragmentList;

    public SectionsPagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragmentList = fragments;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragmentList.get(0);
            case 1:
                return fragmentList.get(1);
            case 2:
                return fragmentList.get(2);
            case 3:
                return fragmentList.get(3);
            case 4:
                return fragmentList.get(4);
            default:
                return fragmentList.get(0);
        }
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
