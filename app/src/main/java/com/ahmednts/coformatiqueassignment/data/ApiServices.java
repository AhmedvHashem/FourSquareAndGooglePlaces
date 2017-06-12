package com.ahmednts.coformatiqueassignment.data;

import com.ahmednts.coformatiqueassignment.data.fouresquare.FoursquareNearbyResponse;
import com.ahmednts.coformatiqueassignment.data.fouresquare.FoursquarePlaceDetailsResponse;
import com.ahmednts.coformatiqueassignment.data.google.GoogleNearbyResponse;
import com.ahmednts.coformatiqueassignment.data.google.GooglePlaceDetailsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by AhmedNTS on 6/1/2017.
 */
public interface ApiServices {

    //types=restaurant
    @GET("nearbysearch/json")
    Observable<GoogleNearbyResponse> googleNearby(@Query("key") String apiKey,
                                                  @Query("location") String location,
                                                  @Query("radius") String radius,
                                                  @Query("types") String types);

    @GET("details/json")
    Observable<GooglePlaceDetailsResponse> googlePlaceDetails(@Query("key") String apiKey,
                                                              @Query("placeid") String placeId);

    //section=food
    @GET("venues/explore")
    Observable<FoursquareNearbyResponse> fouresquareNearby(@Query("client_id") String client_id,
                                                           @Query("client_secret") String client_secret,
                                                           @Query("v") String currentDate/*YYYYMMDD*/,
                                                           @Query("ll") String location,
                                                           @Query("radius") String radius,
                                                           @Query("section") String section);

    @GET("venues/{placeid}")
    Observable<FoursquarePlaceDetailsResponse> fouresquarePlaceDetails(@Path("placeid") String placeId,
                                                                       @Query("client_id") String client_id,
                                                                       @Query("client_secret") String client_secret,
                                                                       @Query("v") String currentDate/*YYYYMMDD*/);
}
