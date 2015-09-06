package com.example.swapnil.myweatherapp.models;


public class WeatherUtil {

    public static String setWeatherIcon(String icon) {

        String iconUrl = "http://openweathermap.org/img/w/";
        iconUrl = iconUrl + icon + ".png";
        return iconUrl;
    }
}
