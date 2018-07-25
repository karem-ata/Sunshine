        package com.example.freewaresys.sunshine;

        import android.content.Context;
        import android.database.Cursor;
        import android.databinding.DataBindingUtil;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.LoaderManager;
        import android.support.v4.content.CursorLoader;
        import android.support.v4.content.Loader;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.support.v4.app.Fragment;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.example.freewaresys.sunshine.data.WeatherContract;
        import com.example.freewaresys.sunshine.sunshineUtils.SunshineDateUtils;
        import com.example.freewaresys.sunshine.sunshineUtils.SunshineWeatherUtils;


        public class DetailsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
        public static final String FRAGMENT_KEY = "URI";

        public static final String[] WeatherDetailsProjection = {
                WeatherContract.WeatherEntry.COLUMN_DATE,
                WeatherContract.WeatherEntry.COLUMN_TEMP_MAX,
                WeatherContract.WeatherEntry.COLUMN_TEMP_MIN,
                WeatherContract.WeatherEntry.COLUMN_WEATHER_CONDITION,
                WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
                WeatherContract.WeatherEntry.COLUMN_PRESSURE,
                WeatherContract.WeatherEntry.COLUMN_WIND_SPEED
        };
        public static final int INDEX_WEATHER_DATE = 0;
        public static final int INDEX_TEMP_MAX = 1;
        public static final int INDEX_TEMP_MIN = 2;
        public static final int INDEX_WEATHER_CONDITION = 3;
        public static final int INDEX_WEATHER_HUMIDITY = 4;
        public static final int INDEX_WEATHER_PRESSURE = 5;
        public static final int INDEX_WEATHER_WIND_SPEED = 6;




        public static final int LOADER_ID = 23;
        private Uri mUri;

        TextView date;
        TextView weatherDescrirption;
        ImageView weatherIcon;
        TextView highTemperature;
        TextView lowTemperature;
        TextView humidity;
        TextView pressure;
        TextView windMeasurement;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            Bundle bundle = this.getArguments();


            if (bundle  != null) {
                String uriString = bundle.getString(FRAGMENT_KEY);
                mUri = Uri.parse(uriString);

            }
            View v = inflater.inflate(R.layout.fragment_details, container, false);
            date = (TextView) v.findViewById(R.id.date);
            weatherDescrirption = (TextView) v.findViewById(R.id.weather_description);
            weatherIcon = (ImageView) v.findViewById(R.id.weather_icon);
            lowTemperature = (TextView) v.findViewById(R.id.low_temperature);
            highTemperature = (TextView) v.findViewById(R.id.high_temperature);
            humidity = (TextView) v.findViewById(R.id.humidity);
            pressure = (TextView) v.findViewById(R.id.pressure);
            windMeasurement = (TextView) v.findViewById(R.id.wind_measurement);

            // Inflate the layout for this fragment

            getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
            return v;


        }
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id != LOADER_ID) {
                return null;
            }

            return new CursorLoader(getActivity(),
                    mUri,
                    WeatherDetailsProjection,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor mCursor) {
            boolean cursorHasValidData = false;
            if (mCursor != null && mCursor.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
                cursorHasValidData = true;
            }

            if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
                return;
            }
            long datee = mCursor.getLong(INDEX_WEATHER_DATE);
            String friendlyDate = SunshineDateUtils.getFriendlyDateString(getActivity(), datee);
            date.setText(friendlyDate);
            String condition = mCursor.getString(INDEX_WEATHER_CONDITION);
            weatherDescrirption.setText(condition);
            int weatherImageResource = SunshineWeatherUtils.getLargeArtResourceIdForWeatherCondition(condition);
            weatherIcon.setImageResource(weatherImageResource);
            double maxTemp = mCursor.getDouble(INDEX_TEMP_MAX);
            String maxTemprature = SunshineWeatherUtils.formateTemperature(getActivity(), maxTemp);
            highTemperature.setText(maxTemprature);
            double minTemp = mCursor.getDouble(INDEX_TEMP_MIN);
            String minTemprature = SunshineWeatherUtils.formateTemperature(getActivity(), minTemp);
            lowTemperature.setText(minTemprature);
            float humidityy = mCursor.getFloat(INDEX_WEATHER_HUMIDITY);
            String weathereHumidity = String.format(this.getString(R.string.format_humidity), humidityy);
            humidity.setText(weathereHumidity);
            float pressuree = mCursor.getFloat(INDEX_WEATHER_PRESSURE);
            String weatherePressure = String.format(this.getString(R.string.format_pressure), pressuree);
            pressure.setText(weatherePressure);
            float wind = mCursor.getFloat(INDEX_WEATHER_WIND_SPEED);
            String weathereWind = String.format(this.getString(R.string.format_wind_kmh), wind);
            windMeasurement.setText(weathereWind);


        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
        }

