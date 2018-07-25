package com.example.freewaresys.sunshine.sync;

import android.os.AsyncTask;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Freeware Sys on 4/13/2017.
 */

public class SunshineFirebaseJobService extends JobService {
    AsyncTask<Void, Void, Void> mFetchWeatherTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mFetchWeatherTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SunshineSyncTask.syncWeather(getApplicationContext());
                jobFinished(jobParameters, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(jobParameters, false);
            }
        };
        mFetchWeatherTask.execute();
        return true;
    }

    /**
 * Called when the scheduling engine has decided to interrupt the execution of a running job,
 * most likely because the runtime constraints associated with the job are no longer satisfied.
 *
 * @return whether the job should be retried
     */
    @Override
    public boolean onStopJob(JobParameters params) {
        if (mFetchWeatherTask != null) {
            mFetchWeatherTask.cancel(true);
        }
        return true;
    }
}

