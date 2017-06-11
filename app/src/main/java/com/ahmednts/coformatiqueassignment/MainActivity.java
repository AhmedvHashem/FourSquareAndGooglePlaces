package com.ahmednts.coformatiqueassignment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ahmednts.coformatiqueassignment.data.ApiClient;
import com.ahmednts.coformatiqueassignment.utils.Logger;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ApiClient.getInstance().googleApiServices.googleNearby(
//                getString(R.string.google_api_key),
//                "30.2446784,31.359881",
//                "5000",
//                "restaurant").subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(googleNearbyResponse -> {
//            Logger.withTag("MainActivity").log(googleNearbyResponse.toString());
//        });
//
//        ApiClient.getInstance().foursquareApiServices.fouresquareNearby(
//                getString(R.string.foursquare_client_id),
//                getString(R.string.foursquare_client_secret),
//                "20161106",
//                "30.2446784,31.359881",
//                "5000",
//                "food").subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(googleNearbyResponse -> {
//            Logger.withTag("MainActivity").log(googleNearbyResponse.toString());
//        });

        ApiClient.getInstance().getNearbyPlaces().subscribe(unifiedPlaceDetails -> {});
    }
}
