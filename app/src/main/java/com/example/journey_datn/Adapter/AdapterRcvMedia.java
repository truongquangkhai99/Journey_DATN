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
import com.example.journey_datn.fragment.FragmentMedia;

import java.util.ArrayList;
import java.util.List;

public class AdapterRcvMedia extends RecyclerView.Adapter<AdapterRcvMedia.ViewHolder> {

    private Context context;
    private AdapterRcvMedia.OnItemClickMediaListener listener;
    private ArrayList<Entity> listEntity;
    private ArrayList<FragmentMedia.itemMedia> listStr;

    public AdapterRcvMedia(Context context,  ArrayList<Entity> listEntity, ArrayList<FragmentMedia.itemMedia> listStr){
        this.context = context;
        this.listEntity = listEntity;
        this.listStr = listStr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_rcv_media, parent, false);
        return new AdapterRcvMedia.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String arrSrc = listStr.get(position).getStrMedia();
        Glide.with(context).load(arrSrc).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(listStr.get(position).getIdMedia());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listStr.size();
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
        void OnItemClick(int id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_item_media);
        }
    }
}
