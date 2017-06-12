package com.ahmednts.coformatiqueassignment.data;

import com.ahmednts.coformatiqueassignment.App;
import com.ahmednts.coformatiqueassignment.R;
import com.ahmednts.coformatiqueassignment.data.fouresquare.FoursquareNearbyResponse;
import com.ahmednts.coformatiqueassignment.data.google.GooglePlaceDetails;
import com.ahmednts.coformatiqueassignment.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static String GOOGLE_PLACES_BASE_URL = "https://maps.googleapis.com/maps/api/place/";
    private static String FOURESQUARE_BASE_URL = "https://api.foursquare.com/v2/";

    private static ApiClient INSTANCE;

    private GoogleAPI googleGoogleAPI;
    private FoursquareAPI foursquareAPI;

    public static ApiClient getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ApiClient();
        return INSTANCE;
    }

    private ApiClient() {
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(GOOGLE_PLACES_BASE_URL)
                .build();

        googleGoogleAPI = retrofit.create(GoogleAPI.class);

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(FOURESQUARE_BASE_URL)
                .build();

        foursquareAPI = retrofit.create(FoursquareAPI.class);
    }

    public Observable<List<UnifiedPlaceDetails>> getNearbyPlaces(String location) {

        Observable<List<UnifiedPlaceDetails>> googleNearbyResponseObservable = googleGoogleAPI
                .googleNearby(App.getInstance().getString(R.string.google_api_key),
                        location,
                        "5000",
                        "restaurant")
                .map(googleNearbyResponse -> {
                    List<UnifiedPlaceDetails> unifiedPlaceDetailsList = new ArrayList<>();
                    for (GooglePlaceDetails googlePlaceDetails : googleNearbyResponse.results) {
                        Logger.withTag("GooglePlaceDetails").log(googlePlaceDetails.toString());
                        unifiedPlaceDetailsList.add(new UnifiedPlaceDetails(UnifiedPlaceDetails.TYPE_GOOGLE,
                                googlePlaceDetails.place_id
                                , googlePlaceDetails.name
                                , googlePlaceDetails.rating
                                , googlePlaceDetails.formatted_address
                                , googlePlaceDetails.formatted_phone_number
                                , googlePlaceDetails.geometry.location.lat
                                , googlePlaceDetails.geometry.location.lng
                                , (googlePlaceDetails.types == null || googlePlaceDetails.types.length == 0) ? "Restaurant" : googlePlaceDetails.types[0]));
                    }
                    return unifiedPlaceDetailsList;
                });

        Observable<List<UnifiedPlaceDetails>> foursquareNearbyResponseObservable = foursquareAPI
                .fouresquareNearby(App.getInstance().getString(R.string.foursquare_client_id), App.getInstance().getString(R.string.foursquare_client_secret),
                        "20161106",
                        location,
                        "5000",
                        "food")
                .map(foursquareNearbyResponse -> {
                    List<UnifiedPlaceDetails> unifiedPlaceDetailsList = new ArrayList<>();
                    for (FoursquareNearbyResponse.Group.Item foursquarePlaceDetails : foursquareNearbyResponse.response.groups.get(0).items) {
                        Logger.withTag("FoursquareNearbyResponse").log(foursquarePlaceDetails.toString());
                        unifiedPlaceDetailsList.add(new UnifiedPlaceDetails(
                                UnifiedPlaceDetails.TYPE_FOURESQUARE,
                                foursquarePlaceDetails.venue.id
                                , foursquarePlaceDetails.venue.name
                                , foursquarePlaceDetails.venue.rating
                                , "No address"
                                , foursquarePlaceDetails.venue.contact.formattedPhone
                                , foursquarePlaceDetails.venue.location.lat
                                , foursquarePlaceDetails.venue.location.lng
                                , (foursquarePlaceDetails.venue.categories == null || foursquarePlaceDetails.venue.categories.length == 0) ? "Restaurant" : foursquarePlaceDetails.venue.categories[0].name));
                    }
                    return unifiedPlaceDetailsList;
                });

        return Observable.merge(googleNearbyResponseObservable, foursquareNearbyResponseObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<UnifiedPlaceDetails> getGooglePlaceDetails(String id) {
        return googleGoogleAPI.googlePlaceDetails(App.getInstance().getString(R.string.google_api_key), id)
                .map(placeDetailsResponse -> {
                    Logger.withTag("GooglePlaceDetails").log(placeDetailsResponse.toString());

                    return new UnifiedPlaceDetails(
                            UnifiedPlaceDetails.TYPE_GOOGLE,
                            placeDetailsResponse.result.place_id
                            , placeDetailsResponse.result.name
                            , placeDetailsResponse.result.rating
                            , placeDetailsResponse.result.formatted_address
                            , placeDetailsResponse.result.formatted_phone_number
                            , placeDetailsResponse.result.geometry.location.lat
                            , placeDetailsResponse.result.geometry.location.lng
                            , (placeDetailsResponse.result.types == null || placeDetailsResponse.result.types.length == 0)
                            ? "Restaurant"
                            : placeDetailsResponse.result.types[0]);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<UnifiedPlaceDetails> getFourSquarePlaceDetails(String id) {
        return foursquareAPI
                .fouresquarePlaceDetails(id, App.getInstance().getString(R.string.foursquare_client_id), App.getInstance().getString(R.string.foursquare_client_secret), "20161106")
                .map(placeDetailsResponse -> {
                    Logger.withTag("FourSquarePlaceDetails").log(placeDetailsResponse.toString());

                    return new UnifiedPlaceDetails(
                            UnifiedPlaceDetails.TYPE_FOURESQUARE,
                            placeDetailsResponse.response.venue.id
                            , placeDetailsResponse.response.venue.name
                            , placeDetailsResponse.response.venue.rating
                            , "No address"
                            , placeDetailsResponse.response.venue.contact.formattedPhone
                            , placeDetailsResponse.response.venue.location.lat
                            , placeDetailsResponse.response.venue.location.lng
                            , (placeDetailsResponse.response.venue.categories == null || placeDetailsResponse.response.venue.categories.length == 0)
                            ? "Restaurant"
                            : placeDetailsResponse.response.venue.categories[0].name);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
