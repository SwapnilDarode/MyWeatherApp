package com.example.swapnil.myweatherapp.dataAdapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swapnil.myweatherapp.ForecastActivity;
import com.example.swapnil.myweatherapp.R;
import com.example.swapnil.myweatherapp.database.CityMap;
import com.example.swapnil.myweatherapp.models.WeatherMap;
import com.example.swapnil.myweatherapp.models.WeatherUtil;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ekhamees on 9/5/15.
 */
public class MultiCityWeatherAdapter extends ArrayAdapter<WeatherMap> {

    Context mContext;
    ArrayList<WeatherMap> map;

    public MultiCityWeatherAdapter(Context context, int res, ArrayList<WeatherMap> list) {
        super(context, res, list);
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
        convertView = inflater.inflate(R.layout.multi_city_weather, parent, false);
        TextView txtCityName, txtToday, txtmaxTemp, txtMinTemp, txtCurrentTemp, txtSunrise, txtSunset, txtWind, txtWeatherInfo;
        ImageView imgWeather;
        ImageButton btnGetInfo;

        final WeatherMap weatherMap = this.map.get(position);


        txtCityName = (TextView) convertView.findViewById(R.id.txtCityName);
        txtToday = (TextView) convertView.findViewById(R.id.txtToday);
        txtCurrentTemp = (TextView) convertView.findViewById(R.id.txtCurrentTemperature);
        txtmaxTemp = (TextView) convertView.findViewById(R.id.txtMaxTemperature);
        txtMinTemp = (TextView) convertView.findViewById(R.id.txtMinTemperature);
        txtSunrise = (TextView) convertView.findViewById(R.id.txtSunrise);
        txtSunset = (TextView) convertView.findViewById(R.id.txtSunset);
        txtWind = (TextView) convertView.findViewById(R.id.txtWind);
        txtWeatherInfo = (TextView) convertView.findViewById(R.id.txtWeatherInfo);
        btnGetInfo = (ImageButton) convertView.findViewById(R.id.btnGetInfo);
        imgWeather = (ImageView) convertView.findViewById(R.id.imgWeather);
        // btnGetInfo.setVisibility(View.GONE);

        txtCityName.setText(weatherMap.name + ", " + weatherMap.sys.country);

        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");

        txtToday.setText(dateFormat.format(new Date()));
        txtWeatherInfo.setText(weatherMap.weather[0].description);
        // &#176;
        txtCurrentTemp.setText(Html.fromHtml("<b>" + weatherMap.main.temp + "&#8451;</b>"));
        txtmaxTemp.setText(Html.fromHtml("<b>" + mContext.getResources().getString(R.string.maxTemp) + " " + weatherMap.main.temp_max + "&#8451;</b>"));
        txtMinTemp.setText(Html.fromHtml("<b>" + mContext.getResources().getString(R.string.minTemp) + " " + weatherMap.main.temp_min + "&#8451;</b>"));

        dateFormat = new SimpleDateFormat("hh:mm a");

        txtSunrise.setText(dateFormat.format(new Date(weatherMap.sys.sunrise * 1000)));

        txtSunset.setText(dateFormat.format(new Date(weatherMap.sys.sunset * 1000)));

        txtWind.setText(mContext.getResources().getString(R.string.windSpeed) + " " + weatherMap.wind.speed + " m/s");

        Picasso.with(mContext).load(WeatherUtil.setWeatherIcon((weatherMap.weather[0].icon))).into(imgWeather);

        btnGetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forecast = new Intent(mContext, ForecastActivity.class);
                forecast.putExtra(CityMap.COLUMN_LAT, Double.parseDouble(weatherMap.coord.lat));
                forecast.putExtra(CityMap.COLUMN_LON, Double.parseDouble(weatherMap.coord.lon));
                mContext.startActivity(forecast);
            }
        });

//        if (position % 2 == 0) {
//            convertView.setBackgroundColor(Color.rgb(235, 235, 235));
//        } else {
//            convertView.setBackgroundColor(Color.WHITE);
//        }

        return convertView;
    }


    Spanned getFoarmatedText(String prefix, String val) {
        return Html.fromHtml("<b>" + prefix + " " + val + "&#8451;</b>");
    }
}
