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
    public static String GOOGLE_PLACES_BASE_URL = "https://maps.googleapis.com/maps/api/place/";
    public static String FOURESQUARE_BASE_URL = "https://api.foursquare.com/v2/";

    private static ApiClient INSTANCE;

    public ApiServices googleApiServices, foursquareApiServices;

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

        googleApiServices = retrofit.create(ApiServices.class);

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(FOURESQUARE_BASE_URL)
                .build();

        foursquareApiServices = retrofit.create(ApiServices.class);
    }

    public Observable<List<UnifiedPlaceDetails>> getNearbyPlaces() {

        Observable<List<UnifiedPlaceDetails>> googleNearbyResponseObservable = googleApiServices
                .googleNearby(App.getInstance().getString(R.string.google_api_key),
                        "30.2446784,31.359881",
                        "5000",
                        "restaurant")
                .map(googleNearbyResponse -> {
                    List<UnifiedPlaceDetails> unifiedPlaceDetailsList = new ArrayList<>();
                    for (GooglePlaceDetails googlePlaceDetails : googleNearbyResponse.results) {
                        Logger.withTag("GooglePlaceDetails").log(googlePlaceDetails.toString());
                        unifiedPlaceDetailsList.add(new UnifiedPlaceDetails(googlePlaceDetails.place_id
                                , googlePlaceDetails.name
                                , googlePlaceDetails.rating
                                , googlePlaceDetails.formatted_address
                                , googlePlaceDetails.formatted_phone_number
                                , googlePlaceDetails.geometry.location.lat
                                , googlePlaceDetails.geometry.location.lng));
                    }
                    return unifiedPlaceDetailsList;
                });

        Observable<List<UnifiedPlaceDetails>> foursquareNearbyResponseObservable = foursquareApiServices
                .fouresquareNearby(App.getInstance().getString(R.string.foursquare_client_id), App.getInstance().getString(R.string.foursquare_client_secret),
                        "20161106",
                        "30.2446784,31.359881",
                        "5000",
                        "food")
                .map(foursquareNearbyResponse -> {
                    List<UnifiedPlaceDetails> unifiedPlaceDetailsList = new ArrayList<>();
                    for (FoursquareNearbyResponse.Group.Item foursquarePlaceDetails : foursquareNearbyResponse.response.groups.get(0).items) {
                        Logger.withTag("FoursquareNearbyResponse").log(foursquarePlaceDetails.toString());
                        unifiedPlaceDetailsList.add(new UnifiedPlaceDetails(foursquarePlaceDetails.venue.id
                                , foursquarePlaceDetails.venue.name
                                , foursquarePlaceDetails.venue.rating
                                , "No address"
                                , foursquarePlaceDetails.venue.contact.formattedPhone
                                , foursquarePlaceDetails.venue.location.lat
                                , foursquarePlaceDetails.venue.location.lng));
                    }
                    return unifiedPlaceDetailsList;
                });

        return Observable.merge(googleNearbyResponseObservable, foursquareNearbyResponseObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

//    public Observable<UnifiedPlaceDetails> getNearbyPlaces() {
//        return googleApiServices
//                .googleNearby(App.getInstance().getString(R.string.google_api_key),
//                        "30.2446784,31.359881",
//                        "5000",
//                        "restaurant")
//                .flatMap(new Function<GoogleNearbyResponse, ObservableSource<? extends FoursquareNearbyResponse>>() {
//                    @Override
//                    public ObservableSource<? extends FoursquareNearbyResponse> apply(@NonNull GoogleNearbyResponse googleNearbyResponse) throws Exception {
//                        return foursquareApiServices.fouresquareNearby(App.getInstance().getString(R.string.foursquare_client_id),
//                                App.getInstance().getString(R.string.foursquare_client_secret),
//                                "20161106",
//                                "30.2446784,31.359881",
//                                "5000",
//                                "food");
//                    }
//                }, new BiFunction<GoogleNearbyResponse, FoursquareNearbyResponse, UnifiedPlaceDetails>() {
//                    @Override
//                    public UnifiedPlaceDetails apply(@NonNull GoogleNearbyResponse googleNearbyResponse, @NonNull FoursquareNearbyResponse foursquareNearbyResponse) throws Exception {
//                        return null;
//                    }
//                });
//    }

//
//    public Observable<Movie> getMovieDetails(int movieId) {
//        return apiServices
//                .movieDetails(movieId, App.API_KEY)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
}
