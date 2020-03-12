package com.example.journey_datn.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.fragment.FragmentItemDetail;
import java.util.List;

public class AdapterPagerDetail extends FragmentPagerAdapter {
    public  List<Entity> listEntity;
    public AdapterPagerDetail(@NonNull FragmentManager fm, List<Entity> listEntity) {
        super(fm);
        this.listEntity = listEntity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return  new FragmentItemDetail(listEntity.get(position));
    }

    @Override
    public int getCount() {
        return listEntity.size();
    }
}
