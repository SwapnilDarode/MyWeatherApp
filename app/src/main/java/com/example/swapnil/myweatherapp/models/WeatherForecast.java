package com.example.swapnil.myweatherapp.models;

import android.util.Log;

import com.example.swapnil.myweatherapp.R;

/**
 * Created by ekhamees on 9/3/15.
 */
public class WeatherForecast {
    public City city;
    public WeatherMap[] list;
    public int code;
    public float message;
    public int cnt;

    public class WeatherMap {

        public long dt;
        public TemperatureDetails temp;
        public double pressure;
        public long humidity;
        public double speed;
        public long deg;
        public com.example.swapnil.myweatherapp.models.WeatherMap.WeatherInfo[] weather;
        public long clouds;
        public double rain;

    }

    /**
     *   Child member class
     */
    public class TemperatureDetails {
        public String min;

        public String eve;

        public String max;

        public String morn;

        public String night;

        public String day;
    }
}
