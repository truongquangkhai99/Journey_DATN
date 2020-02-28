package com.example.journey_datn.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey_datn.R;

import java.util.ArrayList;

public class AdapterRcvAdd extends RecyclerView.Adapter<AdapterRcvAdd.ViewHolder>{

    private Context context;
    private ArrayList<Integer> arrItem = new ArrayList<>();

    public AdapterRcvAdd(Context context){
        this.context = context;
        arrItem.add(R.drawable.icons8_coil_60);
        arrItem.add(R.drawable.icons8_region_80);
        arrItem.add(R.drawable.icons8_thermometer_64);
        arrItem.add(R.drawable.icons8_collect_50);
        arrItem.add(R.drawable.icons8_happy_52);
        arrItem.add(R.drawable.icons8_bold_48);
        arrItem.add(R.drawable.icons8_italic_52);
        arrItem.add(R.drawable.icons8_underline_48);
        arrItem.add(R.drawable.icons8_back_50_2);
        arrItem.add(R.drawable.icons8_forward_48);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_rcv_add, parent, false);
        return new AdapterRcvAdd.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.img_item.setImageResource(arrItem.get(position));
        holder.img_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0)
                    mediaClick();
                if (position == 1)
                    placeClick();
                if (position == 2)
                    temperatureClick();
                if (position == 3)
                    activityClick();
                if (position == 4)
                    faceClick();
                if (position == 5)
                    boldClick();
                if (position == 6)
                    italicClick();
                if (position == 7)
                    underlineClick();
                if (position == 8)
                    backClick();
                if (position == 9)
                    forwardClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrItem.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_item = itemView.findViewById(R.id.image_item_add);
        }
    }

    private void mediaClick(){
        final View dialogView = View.inflate(context,R.layout.dialog_media,null);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(dialogView);

        ImageView imgGallery = dialog.findViewById(R.id.img_dl_gallery);
        ImageView imgFile = dialog.findViewById(R.id.img_dl_file);
        ImageView imgPhoto = dialog.findViewById(R.id.img_dl_photo);
        ImageView imgVideo = dialog.findViewById(R.id.img_dl_video);
        ImageView imgMicrophone = dialog.findViewById(R.id.img_dl_microphone);

        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "choose gallery", Toast.LENGTH_SHORT).show();
            }
        });
        imgFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "choose file", Toast.LENGTH_SHORT).show();
            }
        });
        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "choose photo", Toast.LENGTH_SHORT).show();
            }
        });
        imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "choose video", Toast.LENGTH_SHORT).show();
            }
        });
        imgMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "choose microphone", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void placeClick(){
        final View dialogView = View.inflate(context,R.layout.dialog_pick_place,null);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(dialogView);

        ImageView imgPick = dialog.findViewById(R.id.img_dl_pick_place);
        ImageView imgRename = dialog.findViewById(R.id.img_dl_rename_place_pick_place);
        ImageView imgRemove = dialog.findViewById(R.id.img_dl_remove_place);
        ImageView imgSetup = dialog.findViewById(R.id.img_dl_setup_gps);

        imgPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "choose pick", Toast.LENGTH_SHORT).show();
            }
        });
        imgRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "choose rename", Toast.LENGTH_SHORT).show();
            }
        });
        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "choose remove", Toast.LENGTH_SHORT).show();
            }
        });
        imgSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "choose setup", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void temperatureClick(){
        final View dialogView = View.inflate(context,R.layout.dialog_temperature,null);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(dialogView);

        dialog.show();
    }

    private void activityClick(){
        final View dialogView = View.inflate(context,R.layout.dialog_activity,null);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(dialogView);

        dialog.show();
    }

    private void faceClick(){
        final View dialogView = View.inflate(context,R.layout.dialog_face,null);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(dialogView);

        dialog.show();
    }

    private void boldClick(){
        Toast.makeText(context, "click bold", Toast.LENGTH_SHORT).show();
    }

    private void italicClick(){
        Toast.makeText(context, "click italick", Toast.LENGTH_SHORT).show();
    }

    private void underlineClick(){
        Toast.makeText(context, "click underline", Toast.LENGTH_SHORT).show();
    }

    private void backClick(){
        Toast.makeText(context, "click back", Toast.LENGTH_SHORT).show();
    }

    private void forwardClick(){
        Toast.makeText(context, "click forward", Toast.LENGTH_SHORT).show();
    }

}
