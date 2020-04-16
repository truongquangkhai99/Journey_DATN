package com.example.journey_datn.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey_datn.Model.Diary;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.example.journey_datn.fragment.FragmentAllDiary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdapterRcvAllDiary extends RecyclerView.Adapter<AdapterRcvAllDiary.ViewHolder>{

    private Context context;
    private ArrayList<Diary> listDiary;
    private AdapterRcvAllDiary.onItemClickListener listener;
    private onItemLongClickListener longListener;
    private OnClickItemTab1 onClickItemTab1;
    private boolean checkVisibility;
    private Set<Integer> selectedSet;

    public AdapterRcvAllDiary(Context context, ArrayList<Diary> mDiary, OnClickItemTab1 onClickItemTab1) {
        this.context = context;
        this.listDiary = mDiary;
        this.onClickItemTab1 = onClickItemTab1;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_rcv_all_diary, parent, false);
        checkVisibility = false;
        selectedSet = new HashSet<>();
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Diary pos = listDiary.get(position);
        String[] date = pos.getDate().split("-");
        holder.txtDayRcvDiary.setText("" + date[0]);
        holder.txtMonthRcvDiary.setText("" + date[1]);
        holder.txtYearRcvDiary.setText("" + date[2]);
        holder.txtTitleRcvDiary.setText(pos.getTitle() + "");

        holder.constItemRcvDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheckVisibility()){
                    listDiary.get(position).setCheckRdb(!listDiary.get(position).isCheckRdb());
                    if (listDiary.get(position).isCheckRdb())
                        holder.rdb.setVisibility(View.VISIBLE);
                    else holder.rdb.setVisibility(View.INVISIBLE);
                    holder.rdb.setChecked(!holder.rdb.isChecked());
                    listener.onItemClick(position);
                }else
                    onClickItemTab1.onClickItem(pos.getId(), pos.getTitle(), pos.getContain(), pos.getDate());
            }
        });

        holder.constItemRcvDiary.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.rdb.setVisibility(View.VISIBLE);
                setCheckVisibility(true);
                longListener.onItemLongClick(position);
                return false;
            }
        });

        if (selectedSet.size() > 0) {
            for (int element : selectedSet)
                if (position == element) {
                    listDiary.get(position).setCheckRdb(!listDiary.get(position).isCheckRdb());
                    holder.rdb.setVisibility(View.INVISIBLE);
                    holder.rdb.setChecked(!holder.rdb.isChecked());
                    selectedSet.remove(element);
                    break;
                }
        }
    }

    public ArrayList<Diary> getListDiary() {
        return listDiary;
    }

    @Override
    public int getItemCount() {
        return listDiary.size();
    }

    public void removeData(Set<Integer> set) {
        List<Diary> diaryDelete = new ArrayList<>();
        for (int element : set)
            diaryDelete.add(listDiary.get(element));
        for (Diary diary : diaryDelete)
            listDiary.remove(diary);
        notifyDataSetChanged();
    }

    public void notifiData(Set<Integer> set) {
        notifyDataSetChanged();
        setCheckVisibility(false);
        selectedSet = new HashSet<>();
        selectedSet = set;
    }

    public void setItemClickListener(AdapterRcvAllDiary.onItemClickListener listener) {
        this.listener = listener;
    }

    public void setItemLongClickListener(onItemLongClickListener listener) {
        this.longListener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }


    public  interface onItemLongClickListener{
        void onItemLongClick(int position);
    }

    public interface OnClickItemTab1{
        void onClickItem(int id, String title, String content, String date);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDayRcvDiary, txtMonthRcvDiary, txtYearRcvDiary, txtTitleRcvDiary;
        ConstraintLayout constItemRcvDiary;
        private CheckBox rdb;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDayRcvDiary = itemView.findViewById(R.id.txt_day_diary);
            txtMonthRcvDiary = itemView.findViewById(R.id.txt_month_diary);
            txtYearRcvDiary = itemView.findViewById(R.id.txt_year_diary);
            txtTitleRcvDiary = itemView.findViewById(R.id.txt_title_rcv_diary);
            constItemRcvDiary = itemView.findViewById(R.id.const_item_rcv_diary);
            rdb = itemView.findViewById(R.id.rdb_delete_diary);
        }
    }
}
