package com.example.freewaresys.sunshine.data;

import android.net.Uri;
import android.provider.BaseColumns;

import com.example.freewaresys.sunshine.sunshineUtils.SunshineDateUtils;

/**
 * Created by Freeware Sys on 4/8/2017.
 */

public class WeatherContract {
    public static String CONTENT_AUTHORITY = "com.example.freewaresys.sunshine";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WEATHER = "weather";

    public static final class WeatherEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WEATHER)
                .build();

        static final String TABLE_NAME = "weather";

        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_WIND_SPEED = "wind";

        public static final String COLUMN_TEMP_MAX = "max";
        public static final String COLUMN_TEMP_MIN = "min";

        public static final String COLUMN_WEATHER_CONDITION = "condition";


        /**
         * @param date local date in milliseconds
         * @return Uri to query details about a single weather entry
         */

        public static Uri buildWeatherUriWithDate(long date) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(date))
                    .build();
        }
        public static String getSqlSelectForTodayOnwards() {
            long normalizedGmtNow = SunshineDateUtils.normalizeDate(System.currentTimeMillis());
            return WeatherContract.WeatherEntry.COLUMN_DATE + " >= " + normalizedGmtNow;
        }

    }


}
