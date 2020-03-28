package com.example.journey_datn.fragment.Weather.interfaces;

import com.example.journey_datn.fragment.Weather.models.LocationSearchModel;
import com.example.journey_datn.fragment.Weather.models.OpenWeather5DayModel;
import com.example.journey_datn.fragment.Weather.models.OpenWeatherModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IWeatherApi {
    @GET("weather?type=accurate&units=metric")
    Call<OpenWeatherModel> getOpenWeatherData(@Query("appid") String appId, @Query("q") String query);
    @GET("forecast?type=accurate&units=metric")
    Call<OpenWeather5DayModel> getOpenWeatherData5days(@Query("appid") String appId, @Query("q") String query);
    @GET("locations/v1/cities/autocomplete")
    Call<List<LocationSearchModel>> getAccuWeatherCities(@Query("apikey") String appId,@Query("q") String query);
}
