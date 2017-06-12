package com.ahmednts.coformatiqueassignment.placedetails;

import com.ahmednts.coformatiqueassignment.data.UnifiedPlaceDetails;

/**
 * Created by AhmedNTS on 6/12/2017.
 */

public interface PlaceDetailsContract {

    interface View {
        void showPlaceDetails(UnifiedPlaceDetails unifiedPlaceDetails);

        void showPhoneUI(String number);

        void showDirectionUI(double lat, double lng);

        void showIndicator();

        void hideIndicator();

        void showNoNetworkMessage();
    }

    interface Presenter {

        void loadPlaceDetails(int apiType, String id);

        void callPlaceNumber();

        void openPlaceDirection();

        void stop();
    }
}
