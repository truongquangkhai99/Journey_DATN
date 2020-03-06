package com.example.journey_datn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey_datn.Activity.ItemDetailActivity;
import com.example.journey_datn.Adapter.AdapterRcvEntity;
import com.example.journey_datn.Adapter.AdapterRcvMedia;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FragmentMedia extends Fragment {
    private RecyclerView recyclerView;
    private AdapterRcvMedia adapterRcvMedia;
    private GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
    private FloatingActionButton fabMedia;
    private AdapterRcvEntity adapterRcvEntity;
    private ArrayList<Entity> listEntity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        initView(view);

        fabMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ItemDetailActivity.class);
                startActivityForResult(intent, 911);
            }
        });

        listEntity = new ArrayList<>();
        adapterRcvMedia = new AdapterRcvMedia(getContext(), listEntity);
        recyclerView.setAdapter(adapterRcvMedia);
        recyclerView.setLayoutManager(gridLayoutManager);

//        adapterRcvEntity.getData();

        return view;
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.rcv_media);
        fabMedia = view.findViewById(R.id.fab_media);
    }
}
