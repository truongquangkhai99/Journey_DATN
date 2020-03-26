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

import com.example.journey_datn.Activity.AddDataActivity;
import com.example.journey_datn.Activity.ItemDetailActivity;
import com.example.journey_datn.Adapter.AdapterRcvEntity;
import com.example.journey_datn.Adapter.AdapterRcvMedia;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.example.journey_datn.db.EntityRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class FragmentMedia extends Fragment implements AdapterRcvMedia.OnItemClickMediaListener {
    private RecyclerView recyclerView;
    private AdapterRcvMedia adapterRcvMedia;
    private GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
    private FloatingActionButton fabMedia;
    private ArrayList<Entity> listEntity;
    private EntityRepository entityRepository;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        initView(view);

        fabMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddDataActivity.class);
                startActivityForResult(intent, 911);
            }
        });

        listEntity = new ArrayList<>();
        entityRepository = new EntityRepository(getContext());
        listEntity = (ArrayList<Entity>) entityRepository.getEntity();
        adapterRcvMedia = new AdapterRcvMedia(getContext(), listEntity);
        recyclerView.setAdapter(adapterRcvMedia);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapterRcvMedia.setListener(this);
        return view;
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.rcv_media);
        fabMedia = view.findViewById(R.id.fab_media);
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(getContext(), ItemDetailActivity.class);
        intent.putExtra("entity",  listEntity.get(position));
        intent.putParcelableArrayListExtra("listEntity", listEntity);
        intent.putExtra("position", position);
        startActivityForResult(intent, 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == ItemDetailActivity.RESULT_CODE){
            Entity entity = data.getParcelableExtra("entity");
            entityRepository.updateEntity(entity);
        }
        if (requestCode == 911 && resultCode == AddDataActivity.RESULT_CODE){
            Entity entity = data.getParcelableExtra("entity");
            entityRepository.insertEntity(entity);
        }
    }
}
