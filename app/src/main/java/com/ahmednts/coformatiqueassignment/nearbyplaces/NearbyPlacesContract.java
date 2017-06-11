package com.ahmednts.coformatiqueassignment.nearbyplaces;

import com.ahmednts.coformatiqueassignment.data.UnifiedPlaceDetails;

import java.util.List;

/**
 * Created by AhmedNTS on 6/11/2017.
 */

public interface NearbyPlacesContract {

    interface View {
        void showListView(List<UnifiedPlaceDetails> unifiedPlaceDetailsList);

        void showMapView();

        void showMapViewMarkers(List<UnifiedPlaceDetails> unifiedPlaceDetailsList);

        void openDetailsUI(UnifiedPlaceDetails unifiedPlaceDetails);

        void showIndicator();

        void hideIndicator();

        void showNoResultMessage();

        void showNoNetworkMessage();
    }

    interface Presenter {
        void loadNearbyPlacesList();

        void openPlaceDetails(UnifiedPlaceDetails unifiedPlaceDetails);

        void switchToListView();

        void switchToMapView();

        void mapViewReady();

        void stop();
    }
}
