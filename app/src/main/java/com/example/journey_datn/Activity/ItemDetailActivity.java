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
public class ItemDetailActivity extends AppCompatActivity{

    private ViewPager mViewPager;
    private AdapterPagerDetail adapterPagerDetail;
    private ArrayList<Entity> lstEntity;
    private int position;
    private ImageView img_back_detail, img_star_detail, img_share_detail, img_update_detail, img_menu_detail, imgBefore, imgNext;
    private final static int UPDATE_REQUEST_CODE = 114;
    public static int RESULT_CODE = 115, ACTIVITY_CODE = 2;
    private boolean checkUpdateSearch = false, checkStar = false, checkSee = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        getDataIntent();
        init();
        img_star_detail.setImageResource(lstEntity.get(position).getStar());
        if (lstEntity.get(position).getStar() == R.drawable.ic_star_border_black_24dp) checkStar = false;
        else  checkStar = true;
        img_star_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStar) {
                    img_star_detail.setImageResource(R.drawable.ic_star_border_black_24dp);
                    lstEntity.get(position).setStar(R.drawable.ic_star_border_black_24dp);
                    checkStar = false;
                }
                else {
                    img_star_detail.setImageResource(R.drawable.ic_star_yellow_24dp);
                    lstEntity.get(position).setStar(R.drawable.ic_star_yellow_24dp);
                    checkStar = true;
                }
                checkUpdateSearch = true;
                checkSee = true;
            }
        });

        adapterPagerDetail = new AdapterPagerDetail(getSupportFragmentManager(), lstEntity);
        mViewPager.setAdapter(adapterPagerDetail);
        mViewPager.setCurrentItem(position);

        if (position == 0) imgBefore.setVisibility(View.INVISIBLE);
        if (position == lstEntity.size() - 1) imgNext.setVisibility(View.INVISIBLE);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                if (pos == 0){
                    imgBefore.setVisibility(View.INVISIBLE);
                    imgNext.setVisibility(View.VISIBLE);
                }
                else if (pos == (lstEntity.size() - 1)){
                    imgNext.setVisibility(View.INVISIBLE);
                    imgBefore.setVisibility(View.VISIBLE);
                }
                else {
                    imgBefore.setVisibility(View.VISIBLE);
                    imgNext.setVisibility(View.VISIBLE);
                }
                position = pos;
                img_star_detail.setImageResource(lstEntity.get(position).getStar());
                setPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        imgBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = position - 1;
                mViewPager.setCurrentItem(position);
                imgNext.setVisibility(View.VISIBLE);
                if (position == 0) imgBefore.setVisibility(View.INVISIBLE);
                setPosition(position);
                img_star_detail.setImageResource(lstEntity.get(position).getStar());
            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = position + 1;
                mViewPager.setCurrentItem(position);
                imgBefore.setVisibility(View.VISIBLE);
                if (position == lstEntity.size() - 1) imgNext.setVisibility(View.INVISIBLE);
                setPosition(position);
                img_star_detail.setImageResource(lstEntity.get(position).getStar());
            }
        });

        img_back_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSee){
                    Intent intent = getIntent();
                    intent.putExtra("entity", lstEntity.get(position));
                    intent.putExtra("position", getPosition());
                    intent.putExtra("checkUpdate", checkUpdateSearch);
                    setResult(RESULT_CODE, intent);
                    finish();
                }else finish();
            }
        });

        img_update_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailActivity.this, AddDataActivity.class);
                intent.putExtra("activity", ACTIVITY_CODE);
                intent.putExtra("entityUpdate", lstEntity.get(position));
                intent.putExtra("positionUpdate", getPosition());
                startActivityForResult(intent, UPDATE_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (checkSee){
            Intent intent = getIntent();
            intent.putExtra("entity", lstEntity.get(position));
            intent.putExtra("checkUpdate", checkUpdateSearch);
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
        imgBefore = findViewById(R.id.img_before);
        imgNext = findViewById(R.id.img_next);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == AddDataActivity.RESULT_CODE) {
            checkUpdateSearch = true;
            Entity entity = data.getParcelableExtra("entity");
            Intent intent = getIntent();
            intent.putExtra("entity", entity);
            intent.putExtra("position", getPosition());
            intent.putExtra("checkUpdate", checkUpdateSearch);
            setResult(RESULT_CODE, intent);
            finish();
        }
    }

    public void getDataIntent() {
        Intent intent = getIntent();
        lstEntity = intent.getParcelableArrayListExtra("listEntity");
        position = intent.getIntExtra("position", -1);
        setPosition(position);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
