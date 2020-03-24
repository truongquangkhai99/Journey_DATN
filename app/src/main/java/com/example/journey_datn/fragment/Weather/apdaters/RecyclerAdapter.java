package com.example.journey_datn.fragment.Weather.apdaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey_datn.R;
import com.example.journey_datn.fragment.Weather.models.OpenWeather5DayModel;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    Context context;
    HashMap<String, ArrayList<OpenWeather5DayModel.List>> weatherList = new HashMap<>();

    public RecyclerAdapter(Context context, HashMap<String, ArrayList<OpenWeather5DayModel.List>> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_recycler, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        holder.txtTempMaxC.setVisibility(View.VISIBLE);
        holder.txtTempMinC.setVisibility(View.VISIBLE);
        holder.txtGach.setVisibility(View.VISIBLE);


        Object[] keyset = weatherList.keySet().toArray();
        int tempMin = (int) Math.floor(weatherList.get(String.valueOf(keyset[position])).get(0).getMain().getTempMin());
        int tempMax = (int) Math.ceil(weatherList.get(String.valueOf(keyset[position])).get(weatherList.get(String.valueOf(keyset[position])).size() - 1).getMain().getTempMax());
        holder.txtDate.setText("" + keyset[position]);
        holder.txtTempMin.setText("" + tempMin );
        holder.txtTempMax.setText("" + tempMax);
        holder.txtDescription.setText("" + weatherList.get(String.valueOf(keyset[position])).get(0).getWeather().get(0).getDescription());
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView txtTempMax;
        TextView txtTempMaxC;
        TextView txtTempMin;
        TextView txtTempMinC;
        TextView txtDate;
        TextView txtDescription;
        TextView txtGach;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtTempMax = itemView.findViewById(R.id.txt_tem_max);
            txtDate = itemView.findViewById(R.id.txt_weather_date);
            txtTempMin = itemView.findViewById(R.id.txt_tem_min);
            txtTempMaxC = itemView.findViewById(R.id.txt_tem_max_C);
            txtTempMinC = itemView.findViewById(R.id.txt_tem_min_C);
            txtDescription = itemView.findViewById(R.id.txt_description);
            txtGach = itemView.findViewById(R.id.txt_gach);
        }
    }
}
