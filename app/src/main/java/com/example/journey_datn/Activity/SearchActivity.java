package com.example.journey_datn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class SearchActivity extends AppCompatActivity implements AdapterRcvEntity.onItemLongClickListener, AdapterRcvEntity.onItemClickListener {

    private RecyclerView rcvJourney;
    private int pos;
    private EditText inputSearch;
    public static int REQUEST_CODE = 111;
    public static int RESULT_CODE = 222;
    private AdapterRcvEntity adapterRcvEntity;
    private ArrayList<Entity> lstEntity;
    private ConstraintLayout contraint1;
    private ConstraintLayout contraint2;
    private ImageView imgBack, imgHeart, imgHappy,imgGrinning, imgSad, imgNeutral;
    private EntityRepository entityRepository;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
    private boolean checkUpdateforSearch = false;
    private TextView txtStationary, txtEating, txtWalking, txtRunning, txtBiking, txtAutomotive, txtFlying;
    public static String searchEating = "SearchActionEating", searchStationary = "SearchActionStationary", searchWalking = "SearchActionWalking",
            searchRunning = "SearchActionRunning",searchBiking = "SearchActionBiking", searchAutomotive = "SearchActionAutomotive",
            searchFlying = "SearchActionFlying",searchHeart = "SearchFeelingHeart", searchHappy = "SearchFeelingHappy", searchGrinning = "SearchFeelingGrinning",
            searchSad = "SearchFeelingSad", searchNeutral = "SearchFeelingNeutral";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();

        entityRepository = new EntityRepository(SearchActivity.this);
        entityRepository = new EntityRepository(this);
        lstEntity = (ArrayList<Entity>) entityRepository.getEntity(MainActivity.userId);
        adapterRcvEntity = new AdapterRcvEntity(SearchActivity.this, lstEntity);
        rcvJourney.setAdapter(adapterRcvEntity);
        rcvJourney.setLayoutManager(linearLayoutManager);

        adapterRcvEntity.notifyDataSetChanged();
        adapterRcvEntity.setItemClickListener(this);
        adapterRcvEntity.setItemLongClickListener(this);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("checkUpdate", checkUpdateforSearch);
                setResult(RESULT_CODE, intent);
                finish();
            }
        });

        activityAndFeeling();
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() == 0) {
                    contraint1.setVisibility(View.VISIBLE);
                    contraint2.setVisibility(View.GONE);

                } else {
                    contraint1.setVisibility(View.GONE);
                    contraint2.setVisibility(View.VISIBLE);
                    adapterRcvEntity.getFilter().filter(cs);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    private void init(){
        contraint1 = findViewById(R.id.constaint_1);
        contraint2 = findViewById(R.id.constaint_2);
        imgBack = findViewById(R.id.img_back_search);
        inputSearch = findViewById(R.id.edt_search);
        rcvJourney = findViewById(R.id.search_rcv);
        txtStationary = findViewById(R.id.txt_stationary_search);
        txtEating = findViewById(R.id.txt_eating_search);
        txtWalking = findViewById(R.id.txt_walking_search);
        txtRunning = findViewById(R.id.txt_running_search);
        txtBiking = findViewById(R.id.txt_biking_search);
        txtAutomotive = findViewById(R.id.txt_automotive_search);
        txtFlying = findViewById(R.id.txt_flying_search);
        imgHappy = findViewById(R.id.img_happy_search);
        imgHeart = findViewById(R.id.img_heart_search);
        imgGrinning = findViewById(R.id.img_dl_grinning_search);
        imgNeutral = findViewById(R.id.img_neutral_search);
        imgSad = findViewById(R.id.img_sad_search);
    }

    private void activityAndFeeling(){
        txtStationary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(searchStationary);
            }
        });
        txtEating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(searchEating);
            }
        });
        txtWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(searchWalking);
            }
        });
        txtRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(searchRunning);
            }
        });
        txtBiking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(searchBiking);
            }
        });
        txtAutomotive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(searchAutomotive);
            }
        });
        txtFlying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(searchFlying);
            }
        });

        imgHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(searchHeart);
            }
        });
        imgHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(searchHappy);
            }
        });
        imgSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(searchSad);
            }
        });
        imgNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(searchNeutral);
            }
        });
        imgGrinning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(searchGrinning);
            }
        });
    }

    private void activityClick(String search){
        contraint1.setVisibility(View.GONE);
        contraint2.setVisibility(View.VISIBLE);
        adapterRcvEntity.getFilter().filter(search);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == ItemDetailActivity.RESULT_CODE) {
            Entity entity = data.getParcelableExtra("entity");
            checkUpdateforSearch = data.getBooleanExtra("checkUpdate", false);
            entityRepository.updateEntity(entity);
            adapterRcvEntity.setData(entity, pos);
        }
    }

    @Override
    public void onItemClick(int position) {
        ArrayList<Entity> listFilter;
        pos = position;
        listFilter = adapterRcvEntity.getLstFillter();
        Intent intent = new Intent(SearchActivity.this, ItemDetailActivity.class);
        intent.putExtra("entity", listFilter.get(position));
        intent.putParcelableArrayListExtra("listEntity", listFilter);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onItemLongClick(int position) {
        showDialog(position);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("checkUpdate", checkUpdateforSearch);
        setResult(RESULT_CODE, intent);
        finish();
    }
}
