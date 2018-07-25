package com.example.freewaresys.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.example.freewaresys.sunshine.data.WeatherContract;
import com.example.freewaresys.sunshine.sync.SunshineSyncUtils;

/**
 * Created by Freeware Sys on 3/21/2017.
 */

public class SettingFragment  extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settingfragment);
        //read all preferences from prefScreen
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        //access to how many preferences there
        int count = preferenceScreen.getPreferenceCount();
        //if preferences not equal checkbox, then send to setPreferencesSummary
        for(int i = 0; i < count; i++){
            Preference p = preferenceScreen.getPreference(i);
            if(!(p instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // register the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference p = findPreference(key);
        if(!(p instanceof CheckBoxPreference)){
            String value = sharedPreferences.getString(p.getKey(), "");
            setPreferenceSummary(p, value);
        }
        if(key.equals(getString(R.string.latitude_key))|| key.equals(getString(R.string.longitude_key))){
            SunshineSyncUtils.startImmediateSync(getActivity());
        }
        if (key.equals(getString(R.string.pref_unit_key))) {
            // units have changed. update lists of weather entries accordingly
            getActivity().getContentResolver().notifyChange(WeatherContract.WeatherEntry.CONTENT_URI, null);
        }
    }

    private void setPreferenceSummary(Preference p, Object value){
        String stringValue = value.toString();

        if(p instanceof ListPreference){
            ListPreference preference = (ListPreference) p;
            preference.setSummary(preference.getEntries()[preference.findIndexOfValue(stringValue)]);
        }
        else{
            p.setSummary(stringValue);
        }
    }


}
