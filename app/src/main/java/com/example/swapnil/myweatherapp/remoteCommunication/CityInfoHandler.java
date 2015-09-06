package com.example.swapnil.myweatherapp.remoteCommunication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.swapnil.myweatherapp.R;
import com.example.swapnil.myweatherapp.callbacks.TaskCompleteListener;
import com.example.swapnil.myweatherapp.database.CityDBHandler;
import com.example.swapnil.myweatherapp.models.City;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by ekhamees on 9/2/15.
 */
public class CityInfoHandler {

    Context mContext;
    private ProgressDialog progressDialog;

    ArrayList<City> cityList = new ArrayList<>();


    public CityInfoHandler(Context context) {
        this.mContext = context;
        progressDialog = new ProgressDialog(context);
    }

    public ArrayList<City> getCityInfo(TaskCompleteListener listener) {

        CityInfoTask task = new CityInfoTask(listener);
        task.execute();

        return cityList;
    }

    private class CityInfoTask extends AsyncTask<Void, Void, String> {

        TaskCompleteListener taskCompleteListener;

        public CityInfoTask(TaskCompleteListener listener){
            this.taskCompleteListener = listener;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            BufferedReader br = null;
            try {
                URL url = new URL(mContext.getResources().getString(R.string.cityListAPI));

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = br.readLine();
                line = br.readLine(); // SKIP title row
                while (line != null) {
                    //now create object according to the string
                    StringTokenizer st = new StringTokenizer(line, "\t");

                    cityList.add(getCityMap(st));

                    line = br.readLine();
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            CityDBHandler dbHandler = new CityDBHandler(mContext);
            dbHandler.open();
            for (City city : cityList) {
                dbHandler.addCityInfo(city);
            }
            dbHandler.close();
            taskCompleteListener.onComplete(null);
            progressDialog.dismiss();
        }

        private City getCityMap(StringTokenizer cursor) {


            City cityMap = new City();

            cityMap.id = (cursor.nextToken());
            cityMap.name = (cursor.nextToken());

            cityMap.setCorordinates(cursor.nextToken(), cursor.nextToken());

            if(cursor.countTokens()==5) {
                cityMap.country = (cursor.nextToken());
            }
            return cityMap;
        }
    }
}