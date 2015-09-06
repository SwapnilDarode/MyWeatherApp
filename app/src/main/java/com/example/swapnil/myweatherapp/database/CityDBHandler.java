package com.example.swapnil.myweatherapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.swapnil.myweatherapp.models.City;

import java.util.ArrayList;


public class CityDBHandler {
    private SQLiteDatabase database;
    private CityDBHelper dbHelper;

    public CityDBHandler(Context context) {
        dbHelper = new CityDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ArrayList<City> getAllCities() {
        ArrayList<City> cityList = new ArrayList<City>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + CityMap.TABLE_NAME, null);
        //database.query(CityMap.TABLE_NAME,CityMap.ALL_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            City city = getCityMap(cursor);
            cityList.add(city);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return cityList;
    }

    private City getCityMap(Cursor cursor) {
        City cityMap = new City();

        cityMap.id = (cursor.getString(cursor.getColumnIndex(CityMap.COLUMN_ID)));
        cityMap.name = (cursor.getString(cursor.getColumnIndex(CityMap.COLUMN_NAME)));

        cityMap.setCorordinates(cursor.getString(cursor.getColumnIndex(CityMap.COLUMN_LAT)), cursor.getString(cursor.getColumnIndex(CityMap.COLUMN_LON)));

        cityMap.country = (cursor.getString(cursor.getColumnIndex(CityMap.COLUMN_COUNTRYCODE)));

        return cityMap;
    }

    public void addCityInfo(City city) {
        open();

        ContentValues values = new ContentValues();

        values.put(CityMap.COLUMN_ID, city.id);
        values.put(CityMap.COLUMN_NAME, city.name);
        values.put(CityMap.COLUMN_LAT, city.coord.lat);
        values.put(CityMap.COLUMN_LON, city.coord.lon);
        values.put(CityMap.COLUMN_COUNTRYCODE, city.country);

        database.insert(CityMap.TABLE_NAME, null, values);

        close();

    }

    public ArrayList<City> getCitiesInfo(String[] moreCities) {
        ArrayList<City> cities = new ArrayList<>();
        //String query = "SELECT * FROM " + CityMap.TABLE_NAME + " WHERE name IN (" + getPlaceHolders(moreCities.length) + ")";
//        Cursor cursor = database.rawQuery(query, moreCities); //


        for (String cityName : moreCities) {
            String query = "SELECT * FROM " + CityMap.TABLE_NAME + " WHERE NAME = '" + cityName.trim() + "'";

            Cursor cursor = database.rawQuery(query, null); //

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                City city = getCityMap(cursor);
                cities.add(city);
                cursor.moveToNext();
            }
            cursor.close();
        }


        return cities;
    }

    private String getPlaceHolders(int count) {
        StringBuilder sb = new StringBuilder(count * 2 - 1); // placeholders and between comma - thas it
        sb.append("?");
        for (int i = 1; i < count; i++) {
            sb.append(",?");
        }
        return sb.toString();
    }
}
