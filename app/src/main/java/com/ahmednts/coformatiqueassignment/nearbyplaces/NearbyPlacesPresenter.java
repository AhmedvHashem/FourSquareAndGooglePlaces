package com.ahmednts.coformatiqueassignment.nearbyplaces;

import android.support.annotation.NonNull;

import com.ahmednts.coformatiqueassignment.data.ApiClient;
import com.ahmednts.coformatiqueassignment.data.UnifiedPlaceDetails;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

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
        getNearbyPlacesObserver = ApiClient.getInstance().getNearbyPlaces().subscribe(unifiedPlaceDetails -> {
            nearbyPlacesView.hideIndicator();

            unifiedPlaceDetailsList.addAll(unifiedPlaceDetails);

            if (unifiedPlaceDetailsList.size() > 0)
                nearbyPlacesView.showListView(unifiedPlaceDetailsList);
            else
                nearbyPlacesView.showNoResultMessage();

        }, throwable -> {
            throwable.printStackTrace();
            nearbyPlacesView.showNoNetworkMessage();
        });
    }

    @Override
    public void openPlaceDetails(UnifiedPlaceDetails unifiedPlaceDetails) {

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
        getNearbyPlacesObserver.dispose();
    }
}
