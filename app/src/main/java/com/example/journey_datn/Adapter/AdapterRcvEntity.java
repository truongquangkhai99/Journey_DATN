package com.example.journey_datn.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journey_datn.Activity.SearchActivity;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class AdapterRcvEntity extends RecyclerView.Adapter<AdapterRcvEntity.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Entity> listEntity;
    private onItemClickListener listener;
    private onItemLongClickListener longListener;
    private onCountItemListener countItemListener;
    private ArrayList<Entity> lstFilter;
    private ArrayList<Entity> lstTempFilter = new ArrayList<>();
    private ValueFilter valueFilter;
    private boolean checkVisibility;
    private Set<Integer> selectedSet;
    public ArrayList<Entity> getListFilter() {
        return lstTempFilter;
    }

    public AdapterRcvEntity(Context context, ArrayList<Entity> mEntity) {
        this.context = context;
        this.listEntity = mEntity;
        this.lstFilter = listEntity;
    }

    public boolean isCheckVisibility() {
        return checkVisibility;
    }

    public void setCheckVisibility(boolean checkVisibility) {
        this.checkVisibility = checkVisibility;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_rcv, parent, false);
        checkVisibility = false;
        selectedSet = new HashSet<>();
        countItemListener.onCountItem(listEntity.size());
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Entity entity = listEntity.get(position);
        holder.txt_content_item.setText(entity.getContent());
        holder.txtDateItem.setText(entity.getStrDate());
        holder.txt_th.setText(entity.getTh() + "");
        holder.txt_position_item.setText(entity.getStrPosition());
        if (entity.getTemperature() != 0) {
            holder.txtTemperatureC.setVisibility(View.VISIBLE);
            holder.txt_temperature_item.setVisibility(View.VISIBLE);
            holder.txt_temperature_item.setText(entity.getTemperature() + "");
        }
        if (entity.getMood() != R.drawable.ic_mood_black_24dp) {
            holder.img_mood.setVisibility(View.VISIBLE);
            Glide.with(context).load(entity.getMood()).into(holder.img_mood);
        }
        if (entity.getAction() != R.drawable.ic_action_black_24dp) {
            holder.img_action.setVisibility(View.VISIBLE);
            Glide.with(context).load(entity.getAction()).into(holder.img_action);
        }
        if (!entity.getSrcImage().equals("")) {
            holder.cardviewImg.setVisibility(View.VISIBLE);
            String arrSrc = entity.getSrcImage();
            String[] separated = arrSrc.split(";");
            if (separated.length == 1)
                Glide.with(context).load(arrSrc).into(holder.img_item);
            else
                Glide.with(context).load(separated[0]).into(holder.img_item);
        }
        if (entity.getStar() == R.drawable.ic_star_yellow_24dp) {
            holder.imgStar.setVisibility(View.VISIBLE);
            Glide.with(context).load(entity.getStar()).into(holder.imgStar);
        }
        if (entity.getStar() == R.drawable.ic_star_border_black_24dp)
            holder.imgStar.setVisibility(View.INVISIBLE);

        if (entity.getTextStyle().equals("B"))
            holder.txt_content_item.setTypeface(holder.txt_content_item.getTypeface(), Typeface.BOLD);
        if (entity.getTextStyle().equals("N"))
            holder.txt_content_item.setTypeface(Typeface.DEFAULT);
        if (entity.getTextStyle().equals("I"))
            holder.txt_content_item.setTypeface(holder.txt_content_item.getTypeface(), Typeface.ITALIC);
        if (entity.getTextStyle().equals("U"))
            holder.txt_content_item.setPaintFlags(holder.txt_content_item.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        holder.const_item_layout_rcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheckVisibility()) {
                    listEntity.get(position).setCheckRdb(!listEntity.get(position).isCheckRdb());
                    if (listEntity.get(position).isCheckRdb())
                        holder.rdb.setVisibility(View.VISIBLE);
                    else holder.rdb.setVisibility(View.INVISIBLE);
                    holder.rdb.setChecked(!holder.rdb.isChecked());
                }

                listener.onItemClick(position);
            }
        });

        holder.const_item_layout_rcv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.rdb.setVisibility(View.VISIBLE);
                setCheckVisibility(true);
                longListener.onItemLongClick(position);
                return false;
            }
        });

        if (selectedSet != null && selectedSet.size() > 0) {
            for (int element : selectedSet)
                if (position == element) {
                    listEntity.get(position).setCheckRdb(!listEntity.get(position).isCheckRdb());
                    holder.rdb.setVisibility(View.INVISIBLE);
                    holder.rdb.setChecked(!holder.rdb.isChecked());
                    selectedSet.remove(element);
                    break;
                }
        }
    }

    public ArrayList<Entity> getLstEntity() {
        return listEntity;
    }

    @Override
    public int getItemCount() {
        return listEntity.size();
    }

    public void setItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public void setItemLongClickListener(onItemLongClickListener listener) {
        this.longListener = listener;
    }

    public void notifyData(Set<Integer> set) {
        setCheckVisibility(false);
        selectedSet = new HashSet<>();
        selectedSet = set;
        notifyDataSetChanged();
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

    public void setOnCountItemListener(onCountItemListener event){
        this.countItemListener = event;
    }

    public interface onCountItemListener{
        void onCountItem(int count);
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public interface onItemLongClickListener {
        void onItemLongClick(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_item, img_mood, img_action, imgStar;
        private ConstraintLayout const_item_layout_rcv;
        private TextView txtDateItem, txt_th, txt_content_item, txt_position_item, txt_temperature_item, txtTemperatureC;
        private CardView cardviewImg;
        private CheckBox rdb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_item = itemView.findViewById(R.id.img_item);
            img_mood = itemView.findViewById(R.id.img_icon_happy_item);
            img_action = itemView.findViewById(R.id.img_icon_collect_item);
            txtDateItem = itemView.findViewById(R.id.txt_date_item);
            txt_th = itemView.findViewById(R.id.txt_th);
            txt_content_item = itemView.findViewById(R.id.txt_content_item);
            txt_position_item = itemView.findViewById(R.id.txt_position_item);
            txt_temperature_item = itemView.findViewById(R.id.txt_temperature_item);
            const_item_layout_rcv = itemView.findViewById(R.id.const_item_layout_rcv);
            txtTemperatureC = itemView.findViewById(R.id.txt_temperature_c);
            cardviewImg = itemView.findViewById(R.id.cardview_img);
            imgStar = itemView.findViewById(R.id.img_star_item);
            rdb = itemView.findViewById(R.id.rdb_delete);
        }
    }


    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }


    public class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Entity> filterList = new ArrayList<>();
                for (int i = 0; i < lstFilter.size(); i++)
                    if (lstFilter.get(i).getContent().contains(constraint))
                        filterList.add(lstFilter.get(i));

                if (constraint.equals(SearchActivity.searchStationary)) {
                    int action = R.drawable.ic_accessibility_black_24dp;
                    for (int i = 0; i < lstFilter.size(); i++)
                        if (lstFilter.get(i).getAction() == action)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchEating)) {
                    int action = R.drawable.ic_restaurant_menu_black_24dp;
                    for (int i = 0; i < lstFilter.size(); i++)
                        if (lstFilter.get(i).getAction() == action)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchWalking)) {
                    int action = R.drawable.ic_directions_walk_black_24dp;
                    for (int i = 0; i < lstFilter.size(); i++)
                        if (lstFilter.get(i).getAction() == action)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchRunning)) {
                    int action = R.drawable.ic_directions_run_black_24dp;
                    for (int i = 0; i < lstFilter.size(); i++)
                        if (lstFilter.get(i).getAction() == action)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchBiking)) {
                    int action = R.drawable.ic_directions_bike_black_24dp;
                    for (int i = 0; i < lstFilter.size(); i++)
                        if (lstFilter.get(i).getAction() == action)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchAutomotive)) {
                    int action = R.drawable.ic_directions_car_black_24dp;
                    for (int i = 0; i < lstFilter.size(); i++)
                        if (lstFilter.get(i).getAction() == action)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchFlying)) {
                    int action = R.drawable.ic_airplanemode_active_black_24dp;
                    for (int i = 0; i < lstFilter.size(); i++)
                        if (lstFilter.get(i).getAction() == action)
                            filterList.add(lstFilter.get(i));
                }

                if (constraint.equals(SearchActivity.searchHappy)) {
                    int feeling = R.drawable.ic_happy_red_24dp;
                    for (int i = 0; i < lstFilter.size(); i++)
                        if (lstFilter.get(i).getMood() == feeling)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchHeart)) {
                    int feeling = R.drawable.ic_favorite_red_24dp;
                    for (int i = 0; i < lstFilter.size(); i++)
                        if (lstFilter.get(i).getMood() == feeling)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchSad)) {
                    int feeling = R.drawable.ic_sad_red_24dp;
                    for (int i = 0; i < lstFilter.size(); i++)
                        if (lstFilter.get(i).getMood() == feeling)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchNeutral)) {
                    int feeling = R.drawable.ic_neutral_red_24dp;
                    for (int i = 0; i < lstFilter.size(); i++)
                        if (lstFilter.get(i).getMood() == feeling)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchGrinning)) {
                    int feeling = R.drawable.ic_mood_emoticon_red_24dp;
                    for (int i = 0; i < lstFilter.size(); i++)
                        if (lstFilter.get(i).getMood() == feeling)
                            filterList.add(lstFilter.get(i));
                }
                if (constraint.equals(SearchActivity.searchStar)) {
                    int star = R.drawable.ic_star_yellow_24dp;
                    for (int i = 0; i < lstFilter.size(); i++)
                        if (lstFilter.get(i).getStar() == star)
                            filterList.add(lstFilter.get(i));
                }

                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = lstFilter.size();
                results.values = lstFilter;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listEntity = (ArrayList<Entity>) results.values;
            lstTempFilter = listEntity;
            notifyDataSetChanged();
        }
    }
}
