package com.ahmednts.coformatiqueassignment.data.fouresquare;

/**
 * Created by AhmedNTS on 6/11/2017.
 */
public class FoursquarePlaceDetailsResponse {

    public Meta meta;

    public Response response;

    public static class Meta {
        public int code;
    }

    public static class Response {
        public FoursquarePlaceDetails venue;
    }
}
