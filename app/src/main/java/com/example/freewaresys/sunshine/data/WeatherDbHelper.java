package com.example.freewaresys.sunshine.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.freewaresys.sunshine.data.WeatherContract.WeatherEntry;
/**
 * Created by Freeware Sys on 4/8/2017.
 */

 class WeatherDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sunshineweather.db";
    private static final int DATABASE_VERSION = 3;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String  SQL_CREATE_WEATHER_TABLE =
                "CREATE TABLE " + WeatherEntry.TABLE_NAME       + "(" +
                        WeatherEntry._ID                       + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        WeatherEntry.COLUMN_DATE               + " INTEGER NOT NULL, "                  +
                        WeatherEntry.COLUMN_PRESSURE           + " REAL NOT NULL, "                     +
                        WeatherEntry.COLUMN_HUMIDITY           + " REAL NOT NULL, "                     +
                        WeatherEntry.COLUMN_WIND_SPEED         + " REAL NOT NULL, "                     +

                        WeatherEntry.COLUMN_TEMP_MIN           + " REAL NOT NULL, "                     +
                        WeatherEntry.COLUMN_TEMP_MAX           + " REAL NOT NULL, "                     +

                        WeatherEntry.COLUMN_WEATHER_CONDITION  + " REAL NOT NULL, "                     +

                /*
                 * To ensure this table can only contain one weather entry per date, we declare
                 * the date column to be unique. We also specify "ON CONFLICT REPLACE". This tells
                 * SQLite that if we have a weather entry for a certain date and we attempt to
                 * insert another weather entry with that date, we replace the old weather entry.
                 */
                        " UNIQUE (" + WeatherEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    /**
    * @param sqLiteDatabase sqLiteDatabase that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
