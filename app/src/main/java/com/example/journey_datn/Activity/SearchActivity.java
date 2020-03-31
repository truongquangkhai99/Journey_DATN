package com.example.journey_datn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.journey_datn.Adapter.AdapterRcvEntity;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.example.journey_datn.db.EntityRepository;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements AdapterRcvEntity.onItemLongClickListener, AdapterRcvEntity.onItemClickListener{
    private RecyclerView rcvJourney;
    private int pos;
    private EditText inputSearch;
    public static int REQUEST_CODE = 111;
    public static int RESULT_CODE = 222;
    private AdapterRcvEntity adapterRcvEntity;
    private ArrayList<Entity> lstEntity;
    private ConstraintLayout contraint1;
    private ConstraintLayout contraint2;
    private EntityRepository entityRepository;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        contraint1 = findViewById(R.id.constaint_1);
        contraint2 = findViewById(R.id.constaint_2);
        inputSearch = findViewById(R.id.edt_search);
        entityRepository = new EntityRepository(SearchActivity.this);
        rcvJourney = findViewById(R.id.search_rcv);
        entityRepository = new EntityRepository(this);
        lstEntity = (ArrayList<Entity>) entityRepository.getEntity();
        adapterRcvEntity = new AdapterRcvEntity(SearchActivity.this, lstEntity);
//        lstEntity = adapterRcvEntity.getLstFillter();
        rcvJourney.setAdapter(adapterRcvEntity);
        rcvJourney.setLayoutManager(linearLayoutManager);

        adapterRcvEntity.notifyDataSetChanged();
        adapterRcvEntity.setItemClickListener(this);
        adapterRcvEntity.setItemLongClickListener(this);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if(cs.length()==0) {
                    contraint1.setVisibility(View.VISIBLE);
                    contraint2.setVisibility(View.GONE);

                }else{
                    contraint1.setVisibility(View.GONE);
                    contraint2.setVisibility(View.VISIBLE);
                    SearchActivity.this.adapterRcvEntity.getFilter().filter(cs);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {}
        });
    }
    private void itemClick(int position){
//        Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
        ArrayList<Entity> entities = new ArrayList<>();
        pos = position;
        entities = adapterRcvEntity.getLstFillter();
        Intent intent = new Intent(SearchActivity.this, ItemDetailActivity.class);
        intent.putExtra("entity",  lstEntity.get(position));
        intent.putParcelableArrayListExtra("listEntity", entities);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == ItemDetailActivity.RESULT_CODE){
            Entity entity = data.getParcelableExtra("entity");
            entityRepository.updateEntity(entity);
            adapterRcvEntity.setData(entity, pos);
        }
    }

    private void itemLongClick(int position){
        showDialog(position);
    }
    @Override
    public void onItemClick(int position) {
        itemClick(position);
    }
    @Override
    public void onItemLongClick(int position) {
        itemLongClick(position);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CODE);
        finish();
    }
}
