package com.example.journey_datn.Adapter;

import android.content.Context;
import android.util.Log;
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
    private ArrayList<FragmentMedia.itemMedia> listMedia;
    private ArrayList<itemStr> listStr = new ArrayList<>();

    public AdapterRcvMedia(Context mContext, ArrayList<Entity> mListEntity, ArrayList<FragmentMedia.itemMedia> mListMedia) {
        this.context = mContext;
        this.listEntity = mListEntity;
        this.listMedia = mListMedia;
        setStr();
    }

    private void setStr() {
        for (FragmentMedia.itemMedia itemMedia : listMedia)
            if (!itemMedia.getStrMedia().equals(""))
                listStr.add(new itemStr(itemMedia.getIdMedia(), itemMedia.getStrMedia()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_rcv_media, parent, false);
        return new AdapterRcvMedia.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String arrSrc = listStr.get(position).getSource();
        Glide.with(context).load(arrSrc).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(listStr.get(position).getIdSource());
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
            listEntity.set(position, Entity);
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
        void OnItemClick(String id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_item_media);
        }
    }

    private class itemStr{
        private String idSource;
        private String source;

        public itemStr(String idSource, String source) {
            this.idSource = idSource;
            this.source = source;
        }

        public String getIdSource() {
            return idSource;
        }

        public void setIdSource(String idSource) {
            this.idSource = idSource;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
