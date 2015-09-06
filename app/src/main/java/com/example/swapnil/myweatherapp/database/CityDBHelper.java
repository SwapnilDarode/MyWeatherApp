package com.example.swapnil.myweatherapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ekhamees on 9/2/15.
 */
public class CityDBHelper extends SQLiteOpenHelper {

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "CityMap.db";
    static final String SQL_CREATE_CITYMAPTABLE = "CREATE TABLE " + CityMap.TABLE_NAME + " (" +
            CityMap.COLUMN_ID + " INTEGER PRIMARY KEY," +
            CityMap.COLUMN_NAME + " text, " +
            CityMap.COLUMN_LAT + " text, " +
            CityMap.COLUMN_LON + " text, " +
            CityMap.COLUMN_COUNTRYCODE + " text" +
            " )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + CityMap.TABLE_NAME;

    public CityDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CITYMAPTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
