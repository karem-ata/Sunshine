package com.example.freewaresys.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.freewaresys.sunshine.sunshineUtils.SunshineDateUtils;
import com.example.freewaresys.sunshine.sunshineUtils.SunshineWeatherUtils;



 class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private final Context mContext;
    final private ForecastAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;

     public ForecastAdapter(@NonNull Context context, ForecastAdapterOnClickHandler clickHandler) {
         mContext = context;
         mClickHandler = clickHandler;
    }

    interface ForecastAdapterOnClickHandler {
        void onClick(long date);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }
    @Override
    public int getItemCount() {
        if (null == mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }


    @Override
    public ForecastAdapter.ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        switch (viewType){
            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.forecast_list_item;
                break;
            }
            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.forecast_today_list_item;
                break;
            }


                default:
                    throw new IllegalArgumentException("Invalid view type, value of " + viewType);

        }
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return new ForecastAdapterViewHolder(view);
    }

    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView iconView;

        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;
        final TextView lowTempView;

        ForecastAdapterViewHolder(View view) {
            super(view);

            iconView = (ImageView) view.findViewById(R.id.weather_icon);
            dateView = (TextView) view.findViewById(R.id.date);
            descriptionView = (TextView) view.findViewById(R.id.weather_description);
            highTempView = (TextView) view.findViewById(R.id.high_temperature);
            lowTempView = (TextView) view.findViewById(R.id.low_temperature);

            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            long date = mCursor.getLong(MainFragment.INDEX_WEATHER_DATE);
            mClickHandler.onClick(date);
        }
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        long date = mCursor.getLong(MainFragment.INDEX_WEATHER_DATE);
        String friendlyDate = SunshineDateUtils.getFriendlyDateString(mContext, date);
        holder.dateView.setText(friendlyDate);
        String condition = mCursor.getString(MainFragment.INDEX_WEATHER_CONDITION);
        holder.descriptionView.setText(condition);
        int viewType = getItemViewType(position);
        int weatherImageResource;
        switch (viewType){
            case VIEW_TYPE_FUTURE_DAY:
                weatherImageResource = SunshineWeatherUtils.getSmallArtResourceIdForWeatherCondition(condition);
                break;
            case VIEW_TYPE_TODAY:
                weatherImageResource = SunshineWeatherUtils.getLargeArtResourceIdForWeatherCondition(condition);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
        holder.iconView.setImageResource(weatherImageResource);
        double maxTemp = mCursor.getDouble(MainFragment.INDEX_TEMP_MAX);
        String maxTemprature = SunshineWeatherUtils.formateTemperature(mContext, maxTemp);
        holder.highTempView.setText(maxTemprature);
        double minTemp = mCursor.getDouble(MainFragment.INDEX_TEMP_MIN);
        String minTemprature = SunshineWeatherUtils.formateTemperature(mContext, minTemp);
        holder.lowTempView.setText(minTemprature);


    }
    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
