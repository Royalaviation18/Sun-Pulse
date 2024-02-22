package com.example.sun_pulse

import WeatherApp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    //getting weather as we have the list in the weatherApp as weather
    @GET("weather")
    fun getWeatherData(
        @Query("q")city:String,
        @Query("appid") appid:String,
        @Query("units") units:String
        //calling from weatherapp that's why below statement is written
    ) : Call<WeatherApp>
}