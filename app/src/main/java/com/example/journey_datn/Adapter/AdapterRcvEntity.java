package com.example.journey_datn.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;

import java.util.List;

public class AdapterRcvEntity extends RecyclerView.Adapter<AdapterRcvEntity.ViewHolder> {
    private Context context;
    private List<Entity> lstEntity;
    private OnItemClickListener listener;

    public AdapterRcvEntity(Context context, List<Entity> mEntity) {
        this.context = context;
        this.lstEntity = mEntity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_rcv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Entity pos = lstEntity.get(position);
        Glide.with(context).load(pos.getSrcImage()).into(holder.img_item);
        Glide.with(context).load(pos.getMood()).into(holder.img_mood);
        holder.txt_contain_item.setText(pos.getContent());
        holder.txt_day_item.setText(pos.getDay() + "");
        holder.txt_hour_item.setText(pos.getHour() + "");
        holder.txt_minute_item.setText(pos.getMinute() + "");
        holder.txt_month_item.setText(pos.getMonth() + "");
        holder.txt_position_item.setText(pos.getStrPosition());
        holder.txt_temperature_item.setText(pos.getTemperature() + "");
        holder.txt_th.setText(pos.getTh() + "");
        holder.txt_year_item.setText(pos.getYear() + "");

        holder.const_item_layout_rcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listener.OnItemClick(pos.getSrcImage(),  position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstEntity.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Entity> Entity) {
        if (Entity != null && Entity.size() > 0) {
            lstEntity.clear();
            lstEntity.addAll(Entity);
            notifyDataSetChanged();
        }
    }

    public void addData(List<Entity> Entity) {
        if (Entity != null && Entity.size() > 0) {
            int oldSize = lstEntity.size();
            lstEntity.addAll(Entity);
            notifyItemRangeChanged(oldSize, Entity.size());
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(String source, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_item, img_mood;
        ConstraintLayout const_item_layout_rcv;
        TextView txt_day_item, txt_month_item, txt_year_item, txt_th, txt_hour_item, txt_minute_item,
                txt_contain_item, txt_position_item, txt_temperature_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_item = itemView.findViewById(R.id.img_item);
            img_mood = itemView.findViewById(R.id.img_icon_collect_item);
            txt_day_item = itemView.findViewById(R.id.txt_day_item);
            txt_month_item = itemView.findViewById(R.id.txt_month_item);
            txt_year_item = itemView.findViewById(R.id.txt_year_item);
            txt_th = itemView.findViewById(R.id.txt_th);
            txt_hour_item = itemView.findViewById(R.id.txt_hour_item);
            txt_minute_item = itemView.findViewById(R.id.txt_minute_item);
            txt_contain_item = itemView.findViewById(R.id.txt_contain_item);
            txt_position_item = itemView.findViewById(R.id.txt_position_item);
            txt_temperature_item = itemView.findViewById(R.id.txt_temperature_item);
            const_item_layout_rcv = itemView.findViewById(R.id.const_item_layout_rcv);
        }
    }
}
