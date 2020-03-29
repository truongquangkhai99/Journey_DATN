package com.example.journey_datn.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import java.util.ArrayList;
import java.util.List;

public class AdapterRcvMedia extends RecyclerView.Adapter<AdapterRcvMedia.ViewHolder> {

    private Context context;
    private AdapterRcvMedia.OnItemClickMediaListener listener;
    private ArrayList<Entity> listEntity;

    public AdapterRcvMedia(Context context,  ArrayList<Entity> listEntity){
        this.context = context;
        this.listEntity = listEntity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_rcv_media, parent, false);
        return new AdapterRcvMedia.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(listEntity.get(position).getSrcImage()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listEntity.size();
    }

    public void setListener(AdapterRcvMedia.OnItemClickMediaListener listener) {
        this.listener = listener;
    }

    public void setData(Entity Entity, int position) {
        if (Entity != null) {
            listEntity.remove(position);
            listEntity.add(position, Entity);
            notifyDataSetChanged();
        }
    }

    public void addData(Entity Entity) {
        if (Entity != null) {
            listEntity.add(Entity);
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickMediaListener {
        void OnItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_item_media);
        }
    }
}
