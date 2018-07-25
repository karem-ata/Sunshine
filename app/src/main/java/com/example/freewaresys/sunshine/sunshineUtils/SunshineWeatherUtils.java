package com.example.freewaresys.sunshine.sunshineUtils;

import android.content.Context;

import com.example.freewaresys.sunshine.R;
import com.example.freewaresys.sunshine.data.SunshinePreferences;

/**
 * Created by Freeware Sys on 4/13/2017.
 */

public class SunshineWeatherUtils {

    private static double celsiusToFahrenheit(double temperatureInCelsius) {
        return (temperatureInCelsius * 1.8) + 32;
    }

    public static String formateTemperature(Context context, double temperature) {
        if(!SunshinePreferences.isMetric(context)){
             temperature = celsiusToFahrenheit(temperature);
        }
        int temperatureormateResourceId = R.string.format_temperature;
        return String.format(context.getString(temperatureormateResourceId), temperature);
    }
    public static int getLargeArtResourceIdForWeatherCondition(String condition){
        switch (condition){
            case "Storm":
                return R.drawable.art_storm;
            case "Snow":
                return R.drawable.art_snow;
            case "Rain":
                return R.drawable.art_clear;
            case "Clouds":
               return R.drawable.art_clouds;
            case "Clear":
                return R.drawable.art_clear;
            default:return 1;
        }

    }
    public static int getSmallArtResourceIdForWeatherCondition(String condition){
        switch (condition){
            case "Storm":
                return R.drawable.ic_storm;
            case "Snow":
                return R.drawable.ic_snow;
            case "Rain":
                return R.drawable.ic_rain;
            case "Clouds":
                return R.drawable.ic_cloudy;
            case "Clear":
                return R.drawable.ic_clear;
            default:return 1;
        }

    }
}
