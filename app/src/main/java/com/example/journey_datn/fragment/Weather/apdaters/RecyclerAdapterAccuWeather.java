package com.example.journey_datn.fragment.Weather.apdaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.journey_datn.R;
import com.example.journey_datn.fragment.Weather.models.AccuWeather5DayModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerAdapterAccuWeather extends RecyclerView.Adapter<RecyclerAdapterAccuWeather.RecyclerViewHolder>{
    private Context mContext;
    private List<AccuWeather5DayModel.DailyForecast> mWeatherList = new ArrayList<>();

    public RecyclerAdapterAccuWeather(Context context, AccuWeather5DayModel model) {
        this.mContext = context;
        this.mWeatherList = model.getDailyForecasts();
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_recycler, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {


        try {
            holder.tvWeatherDate.setText("-- " + setDateFormat(mWeatherList.get(position).getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvTempMin.setText("Min Temp - " + String.valueOf(mWeatherList.get(position).getTemperature().getMinimum().getValue()));
        holder.tvTempMax.setText("Max Temp - " + String.valueOf(mWeatherList.get(position).getTemperature().getMaximum().getValue()));
    }

    public String setDateFormat(String unformattedDate) throws ParseException {
        Date dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(unformattedDate);
        return (new SimpleDateFormat("yyyy-MM-dd")).format(dateformat);
    }

    @Override
    public int getItemCount() {
        return mWeatherList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.tv_temp_max)
        TextView tvTempMax;
//        @BindView(R.id.tv_weather_date)
        TextView tvWeatherDate;
//        @BindView(R.id.tv_temp_min)
        TextView tvTempMin;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            tvTempMax = itemView.findViewById(R.id.tv_temp_max);
            tvWeatherDate = itemView.findViewById(R.id.tv_weather_date);
            tvTempMin = itemView.findViewById(R.id.tv_temp_min);
        }
    }
}
