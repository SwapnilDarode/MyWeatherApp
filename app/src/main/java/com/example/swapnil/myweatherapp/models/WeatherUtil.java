package com.example.swapnil.myweatherapp.models;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class WeatherUtil {

    public static String setWeatherIcon(String icon) {

        String iconUrl = "http://openweathermap.org/img/w/";
        iconUrl = iconUrl + icon + ".png";
        return iconUrl;
    }

    public static boolean isNetworkConnectionAvailable(Context context) {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        if (netInfo != null) {
            connected = true;
        } else {
            connected = false;
        }
        return connected;
    }
}
