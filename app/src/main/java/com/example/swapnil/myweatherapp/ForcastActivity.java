package com.example.swapnil.myweatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.swapnil.myweatherapp.callbacks.TaskCompleteListener;
import com.example.swapnil.myweatherapp.dataAdapters.CityForecastAdapter;
import com.example.swapnil.myweatherapp.database.CityMap;
import com.example.swapnil.myweatherapp.models.WeatherForecast;
import com.example.swapnil.myweatherapp.remoteCommunication.WeatherInfoHandler;

import java.util.ArrayList;
import java.util.Arrays;

public class ForcastActivity extends AppCompatActivity {

    String lat, lon;
    static final int days = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forcast);
        setTitle("");
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            lat = String.valueOf(extras.getDouble(CityMap.COLUMN_LAT));
            lon = String.valueOf(extras.getDouble(CityMap.COLUMN_LON));

            populateWeatherForecast();
        }
    }

   void populateWeatherForecast(){
       WeatherInfoHandler weatherInfoHandler = new WeatherInfoHandler(this);
       weatherInfoHandler.getCityWeather(lat,lon,days, new TaskCompleteListener(){
           @Override
           public void onComplete(Object o) {
               WeatherForecast forecast = (WeatherForecast)o;
               if(forecast!=null && forecast.code != 404) {
                   setTitle(forecast.city.name);
                   ListView lstForecast = (ListView) findViewById(R.id.lstForecast);
                   lstForecast.setAdapter(new CityForecastAdapter(ForcastActivity.this, R.layout.forecast_cell, new ArrayList<WeatherForecast.WeatherMap>(Arrays.asList(forecast.list))));
               }else
               {
                   Toast.makeText(ForcastActivity.this, getResources().getString(R.string.oops), Toast.LENGTH_LONG).show();
                   finish();
               }
           }
       });
   }
}
