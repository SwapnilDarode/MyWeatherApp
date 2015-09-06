package com.example.swapnil.myweatherapp.models;

import com.example.swapnil.myweatherapp.R;

/**
 * Created by ekhamees on 9/4/15.
 */
public class WeatherUtil {

    public static String setWeatherIcon(String icon){

        String iconUrl = "http://openweathermap.org/img/w/";
        iconUrl = iconUrl + icon  + ".png";
        return iconUrl;
    }
}
