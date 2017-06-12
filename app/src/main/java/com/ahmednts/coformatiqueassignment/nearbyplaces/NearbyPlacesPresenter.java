package com.ahmednts.coformatiqueassignment.nearbyplaces;

import android.support.annotation.NonNull;

import com.ahmednts.coformatiqueassignment.App;
import com.ahmednts.coformatiqueassignment.data.ApiClient;
import com.ahmednts.coformatiqueassignment.data.UnifiedPlaceDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Created by AhmedNTS on 6/11/2017.
 */

public class NearbyPlacesPresenter implements NearbyPlacesContract.Presenter {

    @NonNull
    private final NearbyPlacesContract.View nearbyPlacesView;

    private List<UnifiedPlaceDetails> unifiedPlaceDetailsList = new ArrayList<>();

    private Disposable getNearbyPlacesObserver;

    public NearbyPlacesPresenter(@NonNull NearbyPlacesContract.View nearbyPlacesView) {
        this.nearbyPlacesView = nearbyPlacesView;
    }

    @Override
    public void loadNearbyPlacesList() {
        nearbyPlacesView.showIndicator();

        getNearbyPlacesObserver = ApiClient.getInstance().getNearbyPlaces(App.getInstance().location.toString()).subscribe(unifiedPlaceDetails -> {
            nearbyPlacesView.hideIndicator();

            unifiedPlaceDetailsList.addAll(unifiedPlaceDetails);

            if (unifiedPlaceDetailsList.size() > 0)
                nearbyPlacesView.showListView(unifiedPlaceDetailsList);
            else
                nearbyPlacesView.showNoResultMessage();

        }, throwable -> {
            throwable.printStackTrace();
            if (throwable instanceof HttpException) {
                // We had non-2XX http error
            } else if (throwable instanceof IOException) {
                // A network  error happened
                nearbyPlacesView.showNoNetworkMessage();
            } else if (throwable instanceof IllegalStateException) {

            }
        });
    }

    @Override
    public void openPlaceDetails(UnifiedPlaceDetails unifiedPlaceDetails) {
        nearbyPlacesView.openDetailsUI(unifiedPlaceDetails);
    }

    @Override
    public void switchToListView() {
        nearbyPlacesView.showListView(unifiedPlaceDetailsList);
    }

    @Override
    public void switchToMapView() {
        nearbyPlacesView.showMapView();
    }

    @Override
    public void mapViewReady() {
        nearbyPlacesView.showMapViewMarkers(unifiedPlaceDetailsList);
    }

    @Override
    public void stop() {
        if (getNearbyPlacesObserver != null)
            getNearbyPlacesObserver.dispose();
    }
}
