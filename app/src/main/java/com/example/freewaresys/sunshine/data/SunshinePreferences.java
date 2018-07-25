package com.example.freewaresys.sunshine.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.freewaresys.sunshine.R;

/**
 * Created by Freeware Sys on 3/31/2017.
 */

public class SunshinePreferences {

    public static float[] coordinates(Context context){

        String longKey = context.getString(R.string.longitude_key);
        float longDefault = 31.3807f;
        String latKey = context.getString(R.string.latitude_key);
        float latDefault = 31.0364f;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        float longitude = Float.valueOf(sharedPreferences.getString(longKey, String.valueOf(longDefault)));
        float latitude = Float.valueOf(sharedPreferences.getString(latKey, String.valueOf(latDefault)));

        float[] coordinates = {(float)longitude, (float)latitude};
        return coordinates;

    }

    public static String unit(Context context){

        String unitKey = context.getString(R.string.pref_unit_key);
        String unitDefault = context.getString(R.string.pref_unit_default);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(unitKey, unitDefault);

    }

    public static boolean isMetric(Context context) {
        String preferredUnits = unit(context);
        String metric = context.getString(R.string.metric);

        boolean userPrefersMetric = false;
        if (metric.equals(preferredUnits)) {
            userPrefersMetric = true;
        }

        return userPrefersMetric;
    }

    public static boolean isNotificationEnables(Context context){

        String notifyKey = context.getString(R.string.notification_key);
        boolean notifyDefault = context.getResources().getBoolean(R.bool.notification_default);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(notifyKey, notifyDefault);

    }
    public static void saveLastNotificationTime(Context context, long timeOfNotification){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("last_notification", timeOfNotification);
        editor.apply();
    }

    private static long getLastNotificationTimeINMillis(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getLong("last_notification", 0);

    }

    public static long getEllapsedTimeSinceLastNotification(Context context){

        long lastNotificationTime = getLastNotificationTimeINMillis(context);
        return System.currentTimeMillis() - lastNotificationTime;
    }

}
