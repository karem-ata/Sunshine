package com.example.freewaresys.sunshine.sync;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.freewaresys.sunshine.data.SunshinePreferences;
import com.example.freewaresys.sunshine.data.WeatherContract.WeatherEntry;
import com.example.freewaresys.sunshine.sunshineUtils.NetworkUtils;
import com.example.freewaresys.sunshine.sunshineUtils.NotificationUtils;
import com.example.freewaresys.sunshine.sunshineUtils.SunshineDateUtils;

import java.net.URL;


/**
 * Created by Freeware Sys on 4/10/2017.
 */

 class SunshineSyncTask {

    private static final String TAG = SunshineSyncTask.class.getSimpleName();

    synchronized public static void syncWeather(Context context) {

        float[] coordinates = SunshinePreferences.coordinates(context);
        String longitude = String.valueOf(coordinates[0]);
        String latitude = String.valueOf(coordinates[1]);
        Log.e(TAG,String.valueOf(longitude));
        String unit = SunshinePreferences.unit(context);
        try {
            URL url = NetworkUtils.buildUrl(longitude, latitude, unit);
                Log.e(TAG, url.toString());
            String weathereJsonString = NetworkUtils.getResponseFromHttpUrl(url);
            ContentValues[] weatherValues = NetworkUtils.getJsonResponse(weathereJsonString);
            Log.e(TAG, String.valueOf(weatherValues.length));
            if(weatherValues == null){
                Log.e(TAG, "nulllllllllllllllllllllllllllllllllllllllllllllll dataaaaaaaaaaaaaaaaaa");
            }
            if (weatherValues != null && weatherValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver sunshineContentResolver = context.getContentResolver();

                /* Delete old weather data because we don't need to keep multiple days' data */
                int x = sunshineContentResolver.delete(
                        WeatherEntry.CONTENT_URI,
                        null,
                        null);
                Log.e(TAG, String.valueOf(x)+"deleteeeeeeeeeeeeeeeeeeeeeed");

                /* Insert our new weather data into Sunshine's ContentProvider */
                int i = sunshineContentResolver.bulkInsert(
                        WeatherEntry.CONTENT_URI,
                        weatherValues);

                Log.e(TAG, String.valueOf(i)+"inserteeeeeeeeeeeeeeeeeeeeeeed");
                boolean areNotificationEnabled = SunshinePreferences.isNotificationEnables(context);

                long ellapsedSinceLastNotification = SunshinePreferences.getEllapsedTimeSinceLastNotification(context);
                boolean onePasedDaySinceLastNotification = false;

                if(ellapsedSinceLastNotification >= SunshineDateUtils.DAY_IN_MILLIS){
                    onePasedDaySinceLastNotification = true;
                }

                if(areNotificationEnabled && onePasedDaySinceLastNotification){
                    NotificationUtils.notifyOfNewWeather(context);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
