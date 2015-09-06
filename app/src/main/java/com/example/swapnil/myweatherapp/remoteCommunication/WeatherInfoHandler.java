package com.example.swapnil.myweatherapp.remoteCommunication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.swapnil.myweatherapp.R;
import com.example.swapnil.myweatherapp.callbacks.TaskCompleteListener;
import com.example.swapnil.myweatherapp.models.MultiCityWeather;
import com.example.swapnil.myweatherapp.models.WeatherForecast;
import com.example.swapnil.myweatherapp.models.WeatherMap;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class WeatherInfoHandler {

    Activity mContext;
    private ProgressDialog progressDialog;

    private final String api_call = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=%s&lon=%s&cnt=%s&mode=json&APPID=%s&units=metric";
    private final String cityWeatherAPI = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&APPID=%s&units=metric&mode=json";
    private final String multiCityWeatherAPI = "http://api.openweathermap.org/data/2.5/group?id=%s,1&APPID=%s&units=metric&mode=json";


    public WeatherInfoHandler(Activity context) {
        this.mContext = context;
        progressDialog = new ProgressDialog(context);
    }

    public void getCityWeather(String lat, String lon, int days, TaskCompleteListener listener) {

        FetchCityWeather asyncTask = new FetchCityWeather(listener);

        asyncTask.execute(lat, lon, String.valueOf(days));
    }

    public void getCurrentCityWeather(String lat, String lon, TaskCompleteListener listener) {
        FetchCurrentCityWeather task = new FetchCurrentCityWeather(listener);
        task.execute(lat, lon);
    }

    public void getNultiCityWeather(String Ids, TaskCompleteListener listener) {
        FectMultiCityWeather multiCityTask = new FectMultiCityWeather(listener);
        multiCityTask.execute(Ids);
    }

    private class FetchCityWeather extends AsyncTask<String, Integer, String> {

        TaskCompleteListener taskCompleteListener;

        WeatherForecast forecast;

        public FetchCityWeather(TaskCompleteListener listener) {
            this.taskCompleteListener = listener;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String weatherData = null;
            String lat = params[0];
            String lon = params[1];
            int forecastForDays = Integer.parseInt(params[2]);

            int progress = 0;
            try {
                publishProgress(progress);
                String callerUrl = String.format(api_call, lat, lon, forecastForDays, mContext.getResources().getString(R.string.weather_map_key));

                Log.e("CALL URL 2", callerUrl);

                URL url = new URL(callerUrl);

                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                JSONObject data = new JSONObject(json.toString());

                forecast = new Gson().fromJson(json.toString(), WeatherForecast.class);

                if (data.getInt("cod") != 200) { // Error response
                    forecast = null;
                }

            } catch (MalformedURLException muEx) {
                weatherData = null;
            } catch (Exception ioEx) {
                weatherData = null;
            }

            return weatherData;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            taskCompleteListener.onComplete(forecast);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    private class FetchCurrentCityWeather extends AsyncTask<String, Void, String> {

        TaskCompleteListener taskCompleteListener;

        public FetchCurrentCityWeather(TaskCompleteListener listener) {
            this.taskCompleteListener = listener;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String weatherData = null;
            String latitude = params[0];
            String longitude = params[1];

            int progress = 0;
            try {
                String callerUrl = String.format(cityWeatherAPI, latitude, longitude, mContext.getResources().getString(R.string.weather_map_key));

                Log.e("CALL URL", callerUrl);

                URL url = new URL(callerUrl);

                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                JSONObject data = new JSONObject(json.toString());

                weatherData = json.toString();

                if (data.getInt("cod") != 200) { // Error response
                    weatherData = null;
                }

            } catch (MalformedURLException muEx) {
                weatherData = null;
            } catch (JSONException jsonEx) {
                weatherData = null;
            } catch (IOException ioEx) {
                weatherData = null;
            }

            return weatherData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            WeatherMap weatherMap = new Gson().fromJson(s, WeatherMap.class);
            this.taskCompleteListener.onComplete(weatherMap);
            progressDialog.dismiss();
        }
    }

    private class FectMultiCityWeather extends AsyncTask<String, Void, String> {

        TaskCompleteListener taskCompleteListener;
        MultiCityWeather multiCityWeather;
        String weatherData;

        public FectMultiCityWeather(TaskCompleteListener listener) {
            this.taskCompleteListener = listener;
        }

        @Override
        protected String doInBackground(String... params) {

            String cityIds = params[0];

            try {
                String callerUrl = String.format(multiCityWeatherAPI, cityIds, mContext.getResources().getString(R.string.weather_map_key));

                Log.e("CALL URL", callerUrl);

                URL url = new URL(callerUrl);

                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                JSONObject data = new JSONObject(json.toString());

                multiCityWeather = new Gson().fromJson(json.toString(), MultiCityWeather.class);
                weatherData = json.toString();

                if (data.getInt("cod") != 200) { // Error response
                    weatherData = null;
                }

            } catch (MalformedURLException muEx) {
                weatherData = null;
            } catch (JSONException jsonEx) {
                weatherData = null;
            } catch (IOException ioEx) {
                weatherData = null;
            }

            return weatherData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            this.taskCompleteListener.onComplete(multiCityWeather);
        }
    }
}
