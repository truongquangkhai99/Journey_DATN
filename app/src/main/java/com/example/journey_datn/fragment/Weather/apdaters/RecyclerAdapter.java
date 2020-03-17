package com.example.journey_datn.fragment.Weather.apdaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.journey_datn.R;
import com.example.journey_datn.fragment.Weather.models.OpenWeather5DayModel;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    Context context;
    HashMap<String, ArrayList<OpenWeather5DayModel.Main>> weatherList = new HashMap<>();

    public RecyclerAdapter(Context context, HashMap<String, ArrayList<OpenWeather5DayModel.Main>> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_recycler, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        Object[] keyset = weatherList.keySet().toArray();
        holder.tvWeatherDate.setText("     " + keyset[position]);
        holder.tvTempMin.setText("Min:   " + weatherList.get(String.valueOf(keyset[position])).get(0).getTempMin());
        holder.tvTempMax.setText("Max:   " + weatherList.get(String.valueOf(keyset[position])).get(weatherList.get(String.valueOf(keyset[position])).size() - 1).getTempMax());
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvTempMax;
        TextView tvWeatherDate;
        TextView tvTempMin;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvTempMax = itemView.findViewById(R.id.tv_temp_max);
            tvWeatherDate = itemView.findViewById(R.id.tv_weather_date);
            tvTempMin = itemView.findViewById(R.id.tv_temp_min);
        }
    }
}
