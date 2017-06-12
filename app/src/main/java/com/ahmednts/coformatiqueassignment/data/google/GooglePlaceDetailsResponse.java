package com.ahmednts.coformatiqueassignment.data.google;

import java.util.List;

/**
 * Created by AhmedNTS on 6/10/2017.
 */

public class GooglePlaceDetailsResponse {

    public GooglePlaceDetails result;
    public String status;

    @Override
    public String toString() {
        return "GooglePlaceDetailsResponse{" +
                "result=" + result +
                ", status='" + status + '\'' +
                '}';
    }
}
