package com.example.journey_datn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.journey_datn.Activity.ImageActivity;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;

public class FragmentItemDetail extends Fragment {

    private ImageView img_item_detail, img_action_detail,img_mood_detail;
    private TextView  txt_day_detail,txt_month_detail,txt_year_detail,txt_th_detail,
            txt_hour_detail,txt_minute_detail,txt_position_detail,txt_temperature_detail,txt_contain_detail, txt_number;
    private ConstraintLayout constraintLayout;
    private Entity entity;
    private  String[] separated;

    public FragmentItemDetail(Entity entity){
        this.entity = entity;
    }

    public Entity getEntity(){
        return  entity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_item, container, false);
        initView(view);
        setData();
        img_item_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ImageActivity.class);
                intent.putExtra("srcImg", separated);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initView(View view){
        img_item_detail = view.findViewById(R.id.img_item_detail);
        img_action_detail = view.findViewById(R.id.img_action_detail);
        img_mood_detail = view.findViewById(R.id.img_mood_detail);
        txt_day_detail = view.findViewById(R.id.txt_day_detail);
        txt_month_detail = view.findViewById(R.id.txt_month_detail);
        txt_year_detail = view.findViewById(R.id.txt_year_detail);
        txt_th_detail = view.findViewById(R.id.txt_th_detail);
        txt_hour_detail = view.findViewById(R.id.txt_hour_detail);
        txt_minute_detail = view.findViewById(R.id.txt_minute_detail);
        txt_position_detail = view.findViewById(R.id.txt_position_detail);
        txt_temperature_detail = view.findViewById(R.id.txt_temperature_detail);
        txt_contain_detail = view.findViewById(R.id.txt_contain_detail);
        txt_number = view.findViewById(R.id.txt_number);
        constraintLayout = view.findViewById(R.id.const_number_item);
    }

    private void setData(){
        String arrSrc = getEntity().getSrcImage();
        separated = arrSrc.split(";");
        if (separated.length == 1){
            Glide.with(getContext()).load(arrSrc).into(img_item_detail);
        }else {
            Glide.with(getContext()).load(separated[0]).into(img_item_detail);
            constraintLayout.setVisibility(View.VISIBLE);
            txt_number.setText("" + separated.length);
        }

        Glide.with(getContext()).load(getEntity().getMood()).into(img_mood_detail);
        Glide.with(getContext()).load(getEntity().getAction()).into(img_action_detail);
        String strDate[] = getEntity().getStrDate().split("-");
        String strYear[] = strDate[2].split(" ");
        String strHour[] = strYear[1].split(":");
        txt_day_detail.setText(strDate[0]+ "");
        txt_month_detail.setText(strDate[1]+ "");
        txt_year_detail.setText(strYear[0]+ "");
        txt_th_detail.setText(getEntity().getTh()+ "");
        txt_hour_detail.setText(strHour[0]+ "");
        txt_minute_detail.setText(strHour[1]+ "");
        txt_temperature_detail.setText(getEntity().getTemperature()+ "");
        txt_position_detail.setText(getEntity().getStrPosition());
        txt_contain_detail.setText(getEntity().getContent());
    }
}
