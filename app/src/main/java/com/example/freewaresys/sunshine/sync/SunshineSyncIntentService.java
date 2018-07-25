package com.example.freewaresys.sunshine.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Freeware Sys on 4/13/2017.
 */

public class SunshineSyncIntentService extends IntentService {
    public SunshineSyncIntentService() {
        super( "SunshineSyncIntentService");}

    @Override
    protected void onHandleIntent(Intent intent) {
        SunshineSyncTask.syncWeather(this);
    }
}
