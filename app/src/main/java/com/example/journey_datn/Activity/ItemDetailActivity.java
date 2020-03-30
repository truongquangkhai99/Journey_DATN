package com.example.journey_datn.Activity;

import android.content.Intent;
import android.os.Bundle;
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
    private ImageView img_back_detail, img_star_detail, img_share_detail, img_print_detail, img_update_detail, img_menu_detail;
    private final static int UPDATE_REQUESTCODE = 114;
    public static int RESULT_CODE = 115;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        getDataIntent();
        init();

        adapterPagerDetail = new AdapterPagerDetail(getSupportFragmentManager(), lstEntity);
        mViewPager.setAdapter(adapterPagerDetail);
        mViewPager.setCurrentItem(position);

        img_back_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    private void init() {
        mViewPager = findViewById(R.id.viewpager_detail);
        img_back_detail = findViewById(R.id.img_back_detail);
        img_star_detail = findViewById(R.id.img_star_detail);
        img_share_detail = findViewById(R.id.img_share_detail);
        img_print_detail = findViewById(R.id.img_print_detail);
        img_update_detail = findViewById(R.id.img_update_detail);
        img_menu_detail = findViewById(R.id.img_menu_detail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUESTCODE && resultCode == AddDataActivity.RESULT_CODE) {
            Entity entity = data.getParcelableExtra("entity");
            Intent intent = getIntent();
            intent.putExtra("entity", entity);
            setResult(RESULT_CODE, intent);
            finish();
            setEntity(entity);
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
