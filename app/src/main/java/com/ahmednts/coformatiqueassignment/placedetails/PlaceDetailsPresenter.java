package com.ahmednts.coformatiqueassignment.placedetails;

import android.support.annotation.NonNull;

import com.ahmednts.coformatiqueassignment.data.ApiClient;
import com.ahmednts.coformatiqueassignment.data.UnifiedPlaceDetails;
import com.ahmednts.coformatiqueassignment.utils.Logger;

import java.io.IOException;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

/**
 * Created by AhmedNTS on 6/12/2017.
 */

public class PlaceDetailsPresenter implements PlaceDetailsContract.Presenter {

    @NonNull
    private final PlaceDetailsContract.View placeDetailsView;

    private UnifiedPlaceDetails unifiedPlaceDetails;

    private Disposable callObserver;

    public PlaceDetailsPresenter(@NonNull PlaceDetailsContract.View placeDetailsView) {
        this.placeDetailsView = placeDetailsView;
    }

    @Override
    public void loadPlaceDetails(int apiType, String id) {
        placeDetailsView.showIndicator();

        Consumer<UnifiedPlaceDetails> onNext = unifiedPlaceDetails -> {
            Logger.withTag("Foursquare UnifiedPlaceDetails1").log(unifiedPlaceDetails.toString());

            placeDetailsView.hideIndicator();

            placeDetailsView.showPlaceDetails(this.unifiedPlaceDetails = unifiedPlaceDetails);
        };

        Consumer<Throwable> onError = throwable -> {
            throwable.printStackTrace();
            if (throwable instanceof HttpException) {
                // We had non-2XX http error
            } else if (throwable instanceof IOException) {
                // A network  error happened
                placeDetailsView.showNoNetworkMessage();
            } else if (throwable instanceof IllegalStateException) {

            }
        };

        if (apiType == UnifiedPlaceDetails.TYPE_GOOGLE)
            callObserver = ApiClient.getInstance().getGooglePlaceDetails(id).subscribe(onNext, onError);
        else {
            callObserver = ApiClient.getInstance().getFourSquarePlaceDetails(id).subscribe(onNext, onError);
        }
    }

    @Override
    public void callPlaceNumber() {
        placeDetailsView.showPhoneUI(unifiedPlaceDetails.getPhone_number());
    }

    @Override
    public void openPlaceDirection() {
        placeDetailsView.showDirectionUI(unifiedPlaceDetails.getLat(), unifiedPlaceDetails.getLng());
    }

    @Override
    public void stop() {
        if (callObserver != null)
            callObserver.dispose();
    }
}
