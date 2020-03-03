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
        arrItem.add(R.drawable.ic_attach_file_black_24dp);
        arrItem.add(R.drawable.ic_location_on_black_24dp);
        arrItem.add(R.drawable.ic_sunny_black_24dp);
        arrItem.add(R.drawable.ic_action_black_24dp);
        arrItem.add(R.drawable.ic_mood_black_24dp);
        arrItem.add(R.drawable.ic_bold_black_24dp);
        arrItem.add(R.drawable.ic_italic_black_24dp);
        arrItem.add(R.drawable.ic_underlined_black_24dp);
        arrItem.add(R.drawable.ic_undo_black_24dp);
        arrItem.add(R.drawable.ic_redo_black_24dp);

    }

    public void updateItem(int position, int value){
        arrItem.set(position, value);
        notifyItemChanged(position);
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
        public ImageView img_item;
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
