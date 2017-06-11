package com.ahmednts.coformatiqueassignment.data.google;

import java.util.List;

/**
 * Created by AhmedNTS on 6/10/2017.
 */

public class GoogleNearbyResponse {

    public List<GooglePlaceDetails> results;
    public String status;

    @Override
    public String toString() {
        return "GoogleNearbyResponse{" +
                "results=" + results +
                ", status='" + status + '\'' +
                '}';
    }
}
