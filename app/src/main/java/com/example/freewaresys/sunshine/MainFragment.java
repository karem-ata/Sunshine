package com.example.freewaresys.sunshine;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.freewaresys.sunshine.data.WeatherContract;
import com.example.freewaresys.sunshine.sync.SunshineSyncUtils;

/**
 * Created by Freeware Sys on 4/25/2017.
 */

public class MainFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ForecastAdapter.ForecastAdapterOnClickHandler {

    RecyclerView recyclerView;
    TextView emptyView;
    public static final int ID_FORECAST_LOADER = 7;
    private ForecastAdapter mForecastAdapter;
    private int mPosition = RecyclerView.NO_POSITION;
    private static final String TAG = MainActivity.class.getSimpleName();
    ProgressBar mIndicator;

    public static final String[] MAIN_FORECAST_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_CONDITION,
            WeatherContract.WeatherEntry.COLUMN_TEMP_MAX,
            WeatherContract.WeatherEntry.COLUMN_TEMP_MIN,
    };

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_CONDITION = 1;
    public static final int INDEX_TEMP_MAX = 2;
    public static final int INDEX_TEMP_MIN = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        emptyView = (TextView) getActivity().findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.forecast_recycleview);
        mIndicator = (ProgressBar) getActivity().findViewById(R.id.indicator);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mForecastAdapter = new ForecastAdapter(getActivity(), this);
        recyclerView.setAdapter(mForecastAdapter);

        showLoading();
        SunshineSyncUtils.initialize(getActivity());
        getActivity().getSupportLoaderManager().initLoader(ID_FORECAST_LOADER, null, this);
    }


    private void checkEmpty() {
        boolean isRecyclerViewEmpty = recyclerView.getAdapter().getItemCount() == 0;

        ConnectivityManager conmagr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conmagr.getActiveNetworkInfo();
        if (networkInfo == null && isRecyclerViewEmpty) {
            emptyView.setText("NO INTERNET CONNECTION");
            mIndicator.setVisibility(View.INVISIBLE);
        }
        emptyView.setVisibility(isRecyclerViewEmpty ? View.VISIBLE : View.GONE);
    }


    private void showLoading() {
        recyclerView.setVisibility(View.INVISIBLE);
        mIndicator.setVisibility(View.VISIBLE);
    }

    private void showWeatherDataView() {
        mIndicator.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {


        switch (loaderId) {

            case ID_FORECAST_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri forecastQueryUri = WeatherContract.WeatherEntry.CONTENT_URI;
                /* Sort order: Ascending by date */
                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                /*
                 * A SELECTION in SQL declares which rows you'd like to return. In our case, we
                 * want all weather data from today onwards that is stored in our weather table.
                 * We created a handy method to do that in our WeatherEntry class.
                 */
                String select = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();

                return new CursorLoader(getActivity(),
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        select,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        mForecastAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        recyclerView.smoothScrollToPosition(mPosition);
        checkEmpty();
        if (data.getCount() != 0) {
            showWeatherDataView();
        }
        if (data.getCount() == 0) {
            Log.e(TAG, "query interupttttttttttttttttttttttttted");
        }
    }


    /**
     * Called when a previously created loader is being reset, and thus making its data unavailable.
     * The application should at this point remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*
         * Since this Loader's data is now invalid, we need to clear the Adapter that is
         * displaying the data.
         */
        Log.e(TAG, "00000000000000000000000000000000000000000000000 reset");
        mForecastAdapter.swapCursor(null);
    }

    @Override
    public void onClick(long date) {
        Uri todayUri = WeatherContract.WeatherEntry.buildWeatherUriWithDate(date);
        DetailsFragment detailsFragment = (DetailsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.details_fragment);
        if (todayUri != null) {
            String myUri = todayUri.toString();

            if (detailsFragment != null) {
                DetailsFragment detailsFragment1 = new DetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(DetailsFragment.FRAGMENT_KEY, myUri);
                detailsFragment1.setArguments(bundle);
                detailsFragment = detailsFragment1;
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.details_fragment, detailsFragment).commit();
            } else {
                Intent detailsActivity = new Intent();
                detailsActivity.setClass(getActivity(), DetailsActivity.class);
                detailsActivity.setData(todayUri);

                startActivity(detailsActivity);
            }
        }
    }
}
