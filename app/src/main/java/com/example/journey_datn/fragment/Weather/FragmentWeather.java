package com.example.journey_datn.fragment.Weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journey_datn.R;
import com.example.journey_datn.fragment.Weather.apdaters.AutoCompleteAdapter;
import com.example.journey_datn.fragment.Weather.apdaters.RecyclerAdapter;
import com.example.journey_datn.fragment.Weather.interfaces.IWeatherApi;
import com.example.journey_datn.fragment.Weather.models.LocationSearchModel;
import com.example.journey_datn.fragment.Weather.models.OpenWeather5DayModel;
import com.example.journey_datn.fragment.Weather.models.OpenWeatherModel;
import com.example.journey_datn.fragment.Weather.utils.ApiService;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.journey_datn.fragment.Weather.constants.ProjectConstants.BASE_URL_OPEN_WEATHER;

public class FragmentWeather extends Fragment{
    private TextView tvCity, tvCountry, tvTemp, tvDescription, tvSpeed;
    private TextView tv,tvC, tvKmh;
    private ImageView ivWeatherIcon;
    private CardView cardView;
    private RecyclerView rvWeatherData;
    private AutoCompleteTextView etCityName;
    private String ACCU_WEATHER_APP_ID = "87ad516d1d4842838fcfebe843d933b1",  OPEN_WEATHER_APP_ID = "b317aca2e83ad16e219ff2283ca837d5";
    private LocationSearchModel mLocationSearchModel;
    private static IWeatherApi mWeatherApi;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        init(view);
        etCityName.setThreshold(2);
        etCityName.setAdapter(new AutoCompleteAdapter(getContext(), ACCU_WEATHER_APP_ID));
        rvWeatherData.setLayoutManager(new LinearLayoutManager(getContext()));
        etCityName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mLocationSearchModel = (LocationSearchModel) adapterView.getAdapter().getItem(i);
                etCityName.setText(mLocationSearchModel.getLocalizedName());
                getOpenWeatherData(etCityName.getText().toString(),  OPEN_WEATHER_APP_ID);
                getOpenWeatherData5Days(etCityName.getText().toString(),  OPEN_WEATHER_APP_ID);
                etCityName.setText("");
            }
        });
        return view;
    }

    private void init(View view){
        tvCity = view.findViewById(R.id.tv_city);
        tvCountry = view.findViewById(R.id.tv_country);
        ivWeatherIcon = view.findViewById(R.id.iv_weather_icon);
        rvWeatherData = view.findViewById(R.id.rv_weather_data);
        etCityName = view.findViewById(R.id.et_city_name);
        tvTemp = view.findViewById(R.id.tv_temp);
        tvDescription = view.findViewById(R.id.tv_description);
        tvSpeed = view.findViewById(R.id.tv_speed);
        tv = view.findViewById(R.id.tv);
        tvC = view.findViewById(R.id.tv_C);
        tvKmh = view.findViewById(R.id.tv_kmh);
        cardView = view.findViewById(R.id.card_view_img);
    }


    public void getWeatherData(Object weatherModel, Boolean success, String errorMsg) {
        if (success) {
            if (weatherModel instanceof OpenWeatherModel) {
                tv.setVisibility(View.VISIBLE);
                tvC.setVisibility(View.VISIBLE);
                tvKmh.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);

                OpenWeatherModel openWeatherModel = (OpenWeatherModel) weatherModel;
                tvCountry.setText("" + openWeatherModel.getSys().getCountry());
                tvCity.setText("" + openWeatherModel.getName());
                tvTemp.setText("" + openWeatherModel.getMain().getTemp());
                tvDescription.setText("" + openWeatherModel.getWeather().get(0).getDescription());
                tvSpeed.setText("" + openWeatherModel.getWind().getSpeed());
                Glide.with(getContext())
                        .load("http://openweathermap.org/img/w/" + openWeatherModel.getWeather().get(0).getIcon() + ".png")
                        .into(ivWeatherIcon);
            } else if (weatherModel instanceof OpenWeather5DayModel) {

                OpenWeather5DayModel weatherBean = (OpenWeather5DayModel) weatherModel;
                try {
                    rvWeatherData.setAdapter(new RecyclerAdapter(getContext(), weatherBean.getMinMaxTemp()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public  void getOpenWeatherData(String city, String appId) {
        mWeatherApi = ApiService.getRetrofitInstance(BASE_URL_OPEN_WEATHER).create(IWeatherApi.class);
        Call<OpenWeatherModel> resForgotPasswordCall = mWeatherApi.getOpenWeatherData(appId, city);
        resForgotPasswordCall.enqueue(new Callback<OpenWeatherModel>() {
            @Override
            public void onResponse(Call<OpenWeatherModel> call, Response<OpenWeatherModel> response) {
                if (response.body() != null) {
                    Object ob = response.body();
                    getWeatherData(ob, true, "");
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherModel> call, Throwable t) {
                getWeatherData(null, false, t.getMessage());
            }
        });
    }

    public void getOpenWeatherData5Days(String city, String appId) {

        mWeatherApi = ApiService.getRetrofitInstance(BASE_URL_OPEN_WEATHER).create(IWeatherApi.class);
        Call<OpenWeather5DayModel> call = mWeatherApi.getOpenWeatherData5days(appId, city);
        call.enqueue(new Callback<OpenWeather5DayModel>() {
            @Override
            public void onResponse(Call<OpenWeather5DayModel> call, Response<OpenWeather5DayModel> response) {
                if (response.body() != null) {
                    Object ob = response.body();
                    getWeatherData(ob, true, "");
                }
            }

            @Override
            public void onFailure(Call<OpenWeather5DayModel> call, Throwable t) {
                getWeatherData(null, false, t.getMessage());
            }
        });
    }
}
