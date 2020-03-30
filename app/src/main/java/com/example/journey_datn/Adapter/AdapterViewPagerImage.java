package com.example.journey_datn.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.journey_datn.fragment.FragmentImage;

public class AdapterViewPagerImage extends FragmentPagerAdapter {
    private String[] src;

    public AdapterViewPagerImage(@NonNull FragmentManager fm, String[] src) {
        super(fm);
        this.src = src;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new FragmentImage(src[position]);
    }

    @Override
    public int getCount() {
        return src.length;
    }

}
