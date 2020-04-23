package com.example.journey_datn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey_datn.Activity.AddDataActivity;
import com.example.journey_datn.Activity.ItemDetailActivity;
import com.example.journey_datn.Activity.MainActivity;
import com.example.journey_datn.Adapter.AdapterRcvMedia;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.example.journey_datn.db.FirebaseDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class FragmentMedia extends Fragment implements AdapterRcvMedia.OnItemClickMediaListener {
    private RecyclerView recyclerView;
    private AdapterRcvMedia adapterRcvMedia;
    private GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
    private FloatingActionButton fabMedia;
    private ArrayList<Entity> listEntity;
    private FirebaseDB firebaseDB = new FirebaseDB(MainActivity.userId);
    private int pos;
    private onDataChangeListenerM listener;


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

        ArrayList<itemMedia> arrItemMedia = new ArrayList<>();
        listEntity = MainActivity.entityList;
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
    public void OnItemClick(String id) {
        for (int i = 0; i < listEntity.size(); i++){
            if (listEntity.get(i).getId().equals(id)){
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
            firebaseDB.updateEntity(entity.getId(), entity);
            adapterRcvMedia.setData(entity, pos);
            loadFragment(new FragmentMedia());
        }
        if (requestCode == 911 && resultCode == AddDataActivity.RESULT_CODE){
            Entity entity = data.getParcelableExtra("entity");
            firebaseDB.insertEntity(entity);
            listener.onDataChangeM(true);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_contain, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setOnDataChangeListener(onDataChangeListenerM listener){
        this.listener = listener;
    }

    public interface onDataChangeListenerM{
        void onDataChangeM(boolean change);
    }


    public class itemMedia{
        private String idMedia;
        private String strMedia;

        public itemMedia(String idMedia, String strMedia) {
            this.idMedia = idMedia;
            this.strMedia = strMedia;
        }

        public String getIdMedia() {
            return idMedia;
        }

        public void setIdMedia(String idMedia) {
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
