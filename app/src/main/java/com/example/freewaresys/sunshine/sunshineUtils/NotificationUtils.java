package com.example.freewaresys.sunshine.sunshineUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import com.example.freewaresys.sunshine.DetailsActivity;
import com.example.freewaresys.sunshine.R;
import com.example.freewaresys.sunshine.data.SunshinePreferences;
import com.example.freewaresys.sunshine.data.WeatherContract.WeatherEntry;

/**
 * Created by Freeware Sys on 4/13/2017.
 */

public class NotificationUtils {

    private static final String[] WEATHER_NOTIFICATION_PROJECTION = {
            WeatherEntry.COLUMN_WEATHER_CONDITION,
            WeatherEntry.COLUMN_TEMP_MAX,
            WeatherEntry.COLUMN_TEMP_MIN,
    };
    private static final int INDEX_WEATHER_CONDITION = 0;
    private static final int INDEX_TEMP_MAX = 1;
    private static final int INDEX_TEMP_MIN = 2;


    private static final int WEATHER_NOTIFICATION_ID = 3004;

    public static void notifyOfNewWeather(Context context) {
        Uri uri = WeatherEntry.buildWeatherUriWithDate(SunshineDateUtils.normalizeDate(System.currentTimeMillis()));
        Cursor todayWeatherDate = context.getContentResolver().query(
                uri,
                WEATHER_NOTIFICATION_PROJECTION,
                null,
                null,
                null
        );
        if (todayWeatherDate.moveToFirst()) {
            String weatherCondition = todayWeatherDate.getString(INDEX_WEATHER_CONDITION);
            double tempMax = todayWeatherDate.getDouble(INDEX_TEMP_MAX);
            double tempMin = todayWeatherDate.getDouble(INDEX_TEMP_MIN);

            int smallIcon = SunshineWeatherUtils.getSmallArtResourceIdForWeatherCondition(weatherCondition);

            Resources resources = context.getResources();
            Bitmap largeIcon = BitmapFactory.decodeResource(resources, smallIcon);
            String notificationTitle = context.getString(R.string.sunshine);

            Intent detailActivity = new Intent(context, DetailsActivity.class);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(detailActivity);
            PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationCompatByuilder = new NotificationCompat.Builder(context)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setSmallIcon(smallIcon)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(notificationTitle)
                    .setContentText(getNotificationText(context, weatherCondition, tempMax, tempMin))
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(WEATHER_NOTIFICATION_ID, notificationCompatByuilder.build());
            SunshinePreferences.saveLastNotificationTime(context, SunshineDateUtils.normalizeDate(System.currentTimeMillis()) + 28800000);
        }
        todayWeatherDate.close();
    }

    private static String getNotificationText(Context context, String condition, double max, double min) {

        String notificationFormate = context.getString(R.string.format_notification);
        return String.format(notificationFormate,
                condition,
                SunshineWeatherUtils.formateTemperature(context, max),
                SunshineWeatherUtils.formateTemperature(context, min));
    }
}
