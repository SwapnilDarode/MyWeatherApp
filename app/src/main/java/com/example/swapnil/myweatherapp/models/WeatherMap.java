package com.example.swapnil.myweatherapp.models;

import android.util.Log;

/**
 * Created by ekhamees on 9/3/15.
 */
public final class WeatherMap {
    public  City.CityCoOrdinates coord;
    public  WeatherInfo weather[];
    public  String base;
    public  Tempearature main;
    public  WindDetails wind;
    public  CloudInfo clouds;
    public  SunRiseSetInfo sys;
    public  long id;
    public  String name;
    public  long cod;

    /**
     *   Child member class
     */
    public class CloudInfo {
        public long all;
    }

    /**
     *   Child member class
     */
    public class SunRiseSetInfo {
        public  long type;
        public  long id;
        public  double message;
        public  String country;
        public  long sunrise;
        public  long sunset;
    }

    /**
     *   Child member class
     */
    public class WeatherInfo {
        public long id;
        public String main;
        public String description;
        public String icon;

     }

    /**
     *   Child member class
     */
    public class WindDetails {
        public double speed;
        public float deg;

    }

    /**
     *   Child member class
     */
    public class Tempearature {
        public float temp;
        public float pressure;
        public float humidity;
        public float temp_min;
        public float temp_max;

    }




}
