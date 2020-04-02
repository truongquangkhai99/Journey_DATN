package com.example.journey_datn.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.journey_datn.Activity.SearchActivity;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import java.util.ArrayList;
import java.util.List;


public class AdapterRcvEntity extends RecyclerView.Adapter<AdapterRcvEntity.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Entity> lstEntity;
    private onItemClickListener listener;
    private onItemLongClickListener longListener;
    private ArrayList<Entity> lstFilter;
    private ArrayList<Entity> lstTempFilter = new ArrayList<>();
    private ValueFilter valueFilter;

    public ArrayList<Entity> getLstFillter() {
        return lstTempFilter;
    }

    public AdapterRcvEntity(Context context, ArrayList<Entity> mEntity) {
        this.context = context;
        this.lstEntity = mEntity;
        this.lstFilter = lstEntity;
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
        holder.txt_contain_item.setText(pos.getContent());
        holder.txt_day_item.setText(pos.getDay() + "");
        holder.txt_hour_item.setText(pos.getHour() + "");
        holder.txt_minute_item.setText(pos.getMinute() + "");
        holder.txt_month_item.setText(pos.getMonth() + "");
        holder.txt_position_item.setText(pos.getStrPosition());
        holder.txt_temperature_item.setText(pos.getTemperature() + "");
        holder.txt_th.setText(pos.getTh() + "");
        holder.txt_year_item.setText(pos.getYear() + "");

        Glide.with(context).load(pos.getMood()).into(holder.img_mood);
        Glide.with(context).load(pos.getAction()).into(holder.img_action);
        String arrSrc = pos.getSrcImage();
        String[] separated = arrSrc.split(";");
        if (separated.length == 1){
            Glide.with(context).load(arrSrc).into(holder.img_item);
        }else {
            Glide.with(context).load(separated[0]).into(holder.img_item);
        }

        holder.const_item_layout_rcv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });

        holder.const_item_layout_rcv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longListener.onItemLongClick(position);
                return false;
            }
        });

    }

    public ArrayList<Entity> getLstEntity() {
        return lstEntity;
    }

    @Override
    public int getItemCount() {
        return lstEntity.size();
    }

    public void setItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
    public void setItemLongClickListener(onItemLongClickListener listener) {
        this.longListener = listener;
    }

    public void setData(Entity Entity, int position) {
        if (Entity != null) {
            lstEntity.remove(position);
            lstEntity.add(position, Entity);
            notifyDataSetChanged();
        }
    }

    public void addData(Entity Entity) {
        if (Entity != null) {
            lstEntity.add(Entity);
            notifyDataSetChanged();
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public  interface onItemLongClickListener{
        void onItemLongClick(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item, img_mood, img_action;
        ConstraintLayout const_item_layout_rcv;
        TextView txt_day_item, txt_month_item, txt_year_item, txt_th, txt_hour_item, txt_minute_item,
                txt_contain_item, txt_position_item, txt_temperature_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_item = itemView.findViewById(R.id.img_item);
            img_mood = itemView.findViewById(R.id.img_icon_happy_item);
            img_action = itemView.findViewById(R.id.img_icon_collect_item);
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

    @Override
    public Filter getFilter() {
        if(valueFilter == null) {
            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }


    public class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint != null && constraint.length()>0){
                ArrayList<Entity> filterList = new ArrayList<>();
                for(int i = 0; i< lstFilter.size(); i++)
                    if(lstFilter.get(i).getContent().contains(constraint))
                        filterList.add(lstFilter.get(i));

                if (constraint.equals(SearchActivity.searchStationary)){
                    int action = R.drawable.ic_accessibility_black_24dp;
                    for(int i = 0; i< lstFilter.size(); i++)
                        if(lstFilter.get(i).getAction() == action)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchEating)){
                    int action = R.drawable.ic_restaurant_menu_black_24dp;
                    for(int i = 0; i< lstFilter.size(); i++)
                        if(lstFilter.get(i).getAction() == action)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchWalking)){
                    int action = R.drawable.ic_directions_walk_black_24dp;
                    for(int i = 0; i< lstFilter.size(); i++)
                        if(lstFilter.get(i).getAction() == action)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchRunning)){
                    int action = R.drawable.ic_directions_run_black_24dp;
                    for(int i = 0; i< lstFilter.size(); i++)
                        if(lstFilter.get(i).getAction() == action)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchBiking)){
                    int action = R.drawable.ic_directions_bike_black_24dp;
                    for(int i = 0; i< lstFilter.size(); i++)
                        if(lstFilter.get(i).getAction() == action)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchAutomotive)){
                    int action = R.drawable.ic_directions_car_black_24dp;
                    for(int i = 0; i< lstFilter.size(); i++)
                        if(lstFilter.get(i).getAction() == action)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchFlying)){
                    int action = R.drawable.ic_airplanemode_active_black_24dp;
                    for(int i = 0; i< lstFilter.size(); i++)
                        if(lstFilter.get(i).getAction() == action)
                            filterList.add(lstFilter.get(i));
                }

                if (constraint.equals(SearchActivity.searchHappy)){
                    int feeling = R.drawable.ic_happy_red_24dp;
                    for(int i = 0; i< lstFilter.size(); i++)
                        if(lstFilter.get(i).getMood() == feeling)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchHeart)){
                    int feeling = R.drawable.ic_favorite_red_24dp;
                    for(int i = 0; i< lstFilter.size(); i++)
                        if(lstFilter.get(i).getMood() == feeling)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchSad)){
                    int feeling = R.drawable.ic_sad_red_24dp;
                    for(int i = 0; i< lstFilter.size(); i++)
                        if(lstFilter.get(i).getMood() == feeling)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchNeutral)){
                    int feeling = R.drawable.ic_neutral_red_24dp;
                    for(int i = 0; i< lstFilter.size(); i++)
                        if(lstFilter.get(i).getMood() == feeling)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchGrinning)){
                    int feeling = R.drawable.ic_mood_emoticon_red_24dp;
                    for(int i = 0; i< lstFilter.size(); i++)
                        if(lstFilter.get(i).getMood() == feeling)
                            filterList.add(lstFilter.get(i));
                }

                results.count=filterList.size();
                results.values=filterList;
            }else{
                results.count= lstFilter.size();
                results.values= lstFilter;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            lstEntity = (ArrayList<Entity>) results.values;
            lstTempFilter = lstEntity;
            notifyDataSetChanged();
        }
    }
}
