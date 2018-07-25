package com.example.freewaresys.sunshine;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class DetailsActivity extends FragmentActivity{

    String myUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Uri uri = getIntent().getData();
        if(uri != null) {
             myUri = uri.toString();
        }
        Bundle bundle = new Bundle();
        bundle.putString(DetailsFragment.FRAGMENT_KEY, myUri);
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_details_fragment, detailsFragment).commit();

    }

}
