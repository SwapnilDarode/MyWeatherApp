package com.example.swapnil.myweatherapp.dataAdapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swapnil.myweatherapp.R;
import com.example.swapnil.myweatherapp.models.WeatherForecast;
import com.example.swapnil.myweatherapp.models.WeatherMap;
import com.example.swapnil.myweatherapp.models.WeatherUtil;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ekhamees on 9/3/15.
 */
public class CityForecastAdapter extends ArrayAdapter<WeatherForecast.WeatherMap> {

    Context mContext;
    ArrayList<WeatherForecast.WeatherMap> map;
    public CityForecastAdapter(Context context,int res, ArrayList<WeatherForecast.WeatherMap> list){
        super(context,res, list);
        this.mContext = context;
        this.map = list;
    }

    @Override
    public int getCount() {
        return this.map.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.forecast_cell, parent, false);

        TextView txtDate = (TextView)convertView.findViewById(R.id.txtDate);
        TextView txtMax = (TextView)convertView.findViewById(R.id.txtMax);
        TextView txtMin = (TextView)convertView.findViewById(R.id.txtMin);
        TextView txtDay = (TextView)convertView.findViewById(R.id.txtDay);
        TextView txtNight = (TextView)convertView.findViewById(R.id.txtNight);
        TextView txtWind = (TextView)convertView.findViewById(R.id.txtWind);
        TextView txtInfo = (TextView)convertView.findViewById(R.id.txtInfo);

        ImageView imgFWeather = (ImageView)convertView.findViewById(R.id.imgFWeather);

        WeatherForecast.WeatherMap wMap = this.map.get(position);

        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        txtDate.setText(dateFormat.format(new Date(wMap.dt * 1000)));

        txtMax.setText(getFoarmatedText(mContext.getResources().getString(R.string.maxTemp), wMap.temp.max));
        txtMin.setText(getFoarmatedText(mContext.getResources().getString(R.string.minTemp), wMap.temp.min));
        txtDay.setText(getFoarmatedText(mContext.getResources().getString(R.string.dayTemp), wMap.temp.day));
        txtNight.setText(getFoarmatedText(mContext.getResources().getString(R.string.nightTemp), wMap.temp.night));

        txtWind.setText(Html.fromHtml("<b>" + (mContext.getResources().getString(R.string.windSpeed) + " " + wMap.speed + " m/s </b>")));

        txtInfo.setText(Html.fromHtml("<b>" + wMap.weather[0].description + "</b>"));

        Picasso.with(mContext).load(WeatherUtil.setWeatherIcon((wMap.weather[0].icon))).into(imgFWeather);

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.rgb(235,235,235));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }


    Spanned getFoarmatedText(String prefix, String val){
        return Html.fromHtml("<b>" + prefix + " " + val + "&#8451;</b>");
    }
}
