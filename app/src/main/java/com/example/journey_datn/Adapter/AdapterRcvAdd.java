package com.example.journey_datn.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey_datn.R;

import java.util.ArrayList;

public class AdapterRcvAdd extends RecyclerView.Adapter<AdapterRcvAdd.ViewHolder>{

    private Context context;
    private ArrayList<Integer> arrItem = new ArrayList<>();
    private OnItemClickListener listener;


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
                listener.OnItemClick(position);
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
    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }
}
