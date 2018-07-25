package com.example.freewaresys.sunshine.sunshineUtils;

import android.content.Context;
import android.text.format.DateUtils;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class SunshineDateUtils {

    public static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);

    public static long normalizeDate(long date){
        long days = TimeUnit.MILLISECONDS.toDays(date);
        return TimeUnit.DAYS.toMillis(days);
    }
    private static long localDate(long date) {
        TimeZone currentTimeZone = TimeZone.getDefault();
        long offSet = currentTimeZone.getOffset(date);
        return offSet + date;
    }

    /**
     * @param utcDate time in miiliSeconds
     * @return days
     */
    private static long millisToDays(long utcDate) {
        return TimeUnit.MILLISECONDS.toDays(utcDate);
    }

    /**
     * @param context Context to use for resource localization
     * @param utcDate The date in milliseconds (utcDAte)
     * @return readable date
     */

    public static String getFriendlyDateString(Context context, long utcDate) {

        long local = localDate(utcDate);
        String readableDate = getReadableDateString(context, local);
        long daysFromEpochToProvidedDate = millisToDays(utcDate);
        long daysFromEpochToToday = millisToDays(System.currentTimeMillis());

        if (daysFromEpochToProvidedDate - daysFromEpochToToday < 2) {
            String dayName = getDayName(utcDate);
            String localizedDayName = DateUtils.formatDateTime(context, utcDate, DateUtils.FORMAT_SHOW_WEEKDAY);
            return readableDate.replace(localizedDayName, dayName);

        }
        return readableDate;
    }

    private static String getReadableDateString(Context context, long localDate) {
        int flags = DateUtils.FORMAT_SHOW_DATE
                | DateUtils.FORMAT_NO_YEAR
                | DateUtils.FORMAT_SHOW_WEEKDAY;


        return DateUtils.formatDateTime(context, localDate, flags);
    }

    private static String getDayName( long dateInMillis) {
        /*
         * If the date is today, return the localized version of "Today" instead of the actual
         * day name.
         */
        long daysFromEpochToProvidedDate = millisToDays(dateInMillis);
        long daysFromEpochToToday = millisToDays(System.currentTimeMillis());

        int currentDay = (int) (daysFromEpochToProvidedDate - daysFromEpochToToday);

        switch (currentDay) {
            case 0:
                return ("today");
            case 1:
                return ("tommorow");

            default:
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                return dayFormat.format(dateInMillis);
        }
    }
}

