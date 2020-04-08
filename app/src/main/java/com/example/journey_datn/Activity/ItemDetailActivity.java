package com.example.journey_datn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.journey_datn.Adapter.AdapterPagerDetail;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;

import java.util.ArrayList;
public class ItemDetailActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private AdapterPagerDetail adapterPagerDetail;
    private ArrayList<Entity> lstEntity;
    private int position;
    private Entity entity;
    private ImageView img_back_detail, img_star_detail, img_share_detail, img_update_detail, img_menu_detail;
    private final static int UPDATE_REQUESTCODE = 114;
    public static int RESULT_CODE = 115;
    private boolean checkUpdateforSearch = false, checkStar = false, checkSee = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        getDataIntent();
        init();
        img_star_detail.setImageResource(getEntity().getStar());
        if (getEntity().getStar() == R.drawable.ic_star_border_black_24dp) checkStar = false;
        else  checkStar = true;
        img_star_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStar) {
                    img_star_detail.setImageResource(R.drawable.ic_star_border_black_24dp);
                    getEntity().setStar(R.drawable.ic_star_border_black_24dp);
                    checkStar = false;
                }
                else {
                    img_star_detail.setImageResource(R.drawable.ic_star_yellow_24dp);
                    getEntity().setStar(R.drawable.ic_star_yellow_24dp);
                    checkStar = true;
                }
                checkUpdateforSearch = true;
                checkSee = true;
            }
        });

        adapterPagerDetail = new AdapterPagerDetail(getSupportFragmentManager(), lstEntity);
        mViewPager.setAdapter(adapterPagerDetail);
        mViewPager.setCurrentItem(position);

        img_back_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSee){
                    Intent intent = getIntent();
                    intent.putExtra("entity", getEntity());
                    intent.putExtra("checkUpdate", checkUpdateforSearch);
                    setResult(RESULT_CODE, intent);
                    finish();
                }else finish();
            }
        });

        img_update_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailActivity.this, AddDataActivity.class);
                intent.putExtra("activity", 2);
                intent.putExtra("entityUpdate", getEntity());
                intent.putExtra("positionUpdate", getPosition());
                startActivityForResult(intent, UPDATE_REQUESTCODE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (checkSee){
            Intent intent = getIntent();
            intent.putExtra("entity", getEntity());
            intent.putExtra("checkUpdate", checkUpdateforSearch);
            setResult(RESULT_CODE, intent);
            finish();
        }else finish();
    }

    private void init() {
        mViewPager = findViewById(R.id.viewpager_detail);
        img_back_detail = findViewById(R.id.img_back_detail);
        img_star_detail = findViewById(R.id.img_star_detail);
        img_share_detail = findViewById(R.id.img_share_detail);
        img_update_detail = findViewById(R.id.img_update_detail);
        img_menu_detail = findViewById(R.id.img_menu_detail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUESTCODE && resultCode == AddDataActivity.RESULT_CODE) {
            checkUpdateforSearch = true;
            Entity entity = data.getParcelableExtra("entity");
            Intent intent = getIntent();
            intent.putExtra("entity", entity);
            intent.putExtra("checkUpdate", checkUpdateforSearch);
            setResult(RESULT_CODE, intent);
            finish();
        }
    }


    public void getDataIntent() {
        Intent intent = getIntent();
        Entity entity = intent.getParcelableExtra("entity");
        lstEntity = intent.getParcelableArrayListExtra("listEntity");
        position = intent.getIntExtra("position", -1);
        setEntity(entity);
        setPosition(position);
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
