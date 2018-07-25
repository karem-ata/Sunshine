package com.example.freewaresys.sunshine.sunshineUtils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.freewaresys.sunshine.data.WeatherContract.WeatherEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Freeware Sys on 3/27/2017.
 */

public class NetworkUtils {
    private static final String OWM_LIST = "list";
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String OWM_DATE = "dt";
    private static final String OWM_PRESSURE = "pressure";
    private static final String OWM_HUMIDITY = "humidity";
    private static final String OWM_WINDSPEED = "speed";

    private static final String OWM_TEMPERATURE = "temp";

    private static final String OWM_TEMP_MAX = "max";
    private static final String OWM_TEMP_MIN = "min";

    private static final String OWM_WEATHER = "weather";
    private static final String OWM_WEATHER_CONDITION = "main";


    private static final String BASE_WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?APPID=ea2ff0079ab81caff51e2523bfc376d7&cnt=14";
    private static final String LATITUDE_PARAM = "lat";
    private static final String LONGITUDE_PARAM = "lon";
    private static final String UNITS_PARAM = "units";

    public static URL buildUrl(String longitude, String latitude, String units) {
        Uri uri = Uri.parse(BASE_WEATHER_URL).buildUpon()
                .appendQueryParameter(LATITUDE_PARAM, latitude)
                .appendQueryParameter(LONGITUDE_PARAM, longitude)
                .appendQueryParameter(UNITS_PARAM, units)
                .build();
        try {
            return new URL(uri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            Log.e(TAG, String.valueOf(url));
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
    public static ContentValues[] getJsonResponse(String forecastJsonString) throws JSONException{

       JSONObject forecastObject = new JSONObject(forecastJsonString);

                    int errorCode = forecastObject.getInt("cod");

                    switch (errorCode) {
                        case 200:
                            break;

                        default:
                    /* Server probably down */
                            Log.e(TAG,"error respooooooooooooooooooooooooooooooooooooooooooooooooooonse");
                    }
                    JSONArray jsonWeatherArray = forecastObject.getJSONArray(OWM_LIST);
                    ContentValues[] weatherContentValues = new ContentValues[jsonWeatherArray.length()];
                    for (int i = 0; i < jsonWeatherArray.length(); i++) {

                        long dt;
                        long date;
                        double pressure;
                        int humidity;
                        double speed;
                        double max;
                        double min;
                        String main;

                        JSONObject dayForecast = jsonWeatherArray.getJSONObject(i);
                        date = dayForecast.getLong(OWM_DATE) * 1000;
                        date = SunshineDateUtils.normalizeDate(date);
                        pressure = dayForecast.getDouble(OWM_PRESSURE);
                        humidity = dayForecast.getInt(OWM_HUMIDITY);
                        speed = dayForecast.getDouble(OWM_WINDSPEED);

                        JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                        max = temperatureObject.getDouble(OWM_TEMP_MAX);
                        min = temperatureObject.getDouble(OWM_TEMP_MIN);

                        JSONObject weatherObject =
                                dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                        main = weatherObject.getString(OWM_WEATHER_CONDITION);

                        ContentValues weatherDayValues = new ContentValues();

                        weatherDayValues.put(WeatherEntry.COLUMN_DATE, date);
                        weatherDayValues.put(WeatherEntry.COLUMN_PRESSURE, pressure);
                        weatherDayValues.put(WeatherEntry.COLUMN_HUMIDITY, humidity);
                        weatherDayValues.put(WeatherEntry.COLUMN_WIND_SPEED, speed);
                        weatherDayValues.put(WeatherEntry.COLUMN_TEMP_MAX, max);
                        weatherDayValues.put(WeatherEntry.COLUMN_TEMP_MIN, min);
                        weatherDayValues.put(WeatherEntry.COLUMN_WEATHER_CONDITION, main);
                        weatherContentValues[i] = weatherDayValues;
                    }

        return weatherContentValues;

    }
}