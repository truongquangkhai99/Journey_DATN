package com.example.journey_datn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private int pos;


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
        ArrayList<itemMedia> arrItemMedia = new ArrayList<>();
        entityRepository = new EntityRepository(getContext());
        listEntity = (ArrayList<Entity>) entityRepository.getEntity();
        for (Entity entity : listEntity){
            String arrSrc = entity.getSrcImage();
            String[] separated = arrSrc.split(";");
            if (separated.length == 1){
                arrItemMedia.add(new itemMedia(entity.getId(), arrSrc));
            }else {
                for (String str : separated){
                    arrItemMedia.add(new itemMedia(entity.getId(), str));
                }
            }
        }
        adapterRcvMedia = new AdapterRcvMedia(getContext(), listEntity, arrItemMedia);
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
    public void OnItemClick(int id) {
        for (int i = 0; i < listEntity.size(); i++){
            if (listEntity.get(i).getId() == id){
                pos = i;
                break;
            }
        }
        Intent intent = new Intent(getContext(), ItemDetailActivity.class);
        intent.putExtra("entity",  listEntity.get(pos));
        intent.putParcelableArrayListExtra("listEntity", listEntity);
        intent.putExtra("position", pos);
        startActivityForResult(intent, 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == ItemDetailActivity.RESULT_CODE){
            Entity entity = data.getParcelableExtra("entity");
            entityRepository.updateEntity(entity);
            adapterRcvMedia.setData(entity, pos);
        }
        if (requestCode == 911 && resultCode == AddDataActivity.RESULT_CODE){
            Entity entity = data.getParcelableExtra("entity");
            entityRepository.insertEntity(entity);
            adapterRcvMedia.addData(entity);
        }
    }

    public class itemMedia{
        private int idMedia;
        private String strMedia;

        public itemMedia(int idMedia, String strMedia) {
            this.idMedia = idMedia;
            this.strMedia = strMedia;
        }

        public int getIdMedia() {
            return idMedia;
        }

        public void setIdMedia(int idMedia) {
            this.idMedia = idMedia;
        }

        public String getStrMedia() {
            return strMedia;
        }

        public void setStrMedia(String strMedia) {
            this.strMedia = strMedia;
        }
    }
}
