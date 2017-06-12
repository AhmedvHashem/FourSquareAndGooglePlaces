package com.ahmednts.coformatiqueassignment.data.google;

import java.util.Arrays;

/**
 * Created by AhmedNTS on 6/11/2017.
 */

public class GooglePlaceDetails {

    public String place_id;
    public String name;

    public double rating;

    public Geometry geometry;

    public String formatted_address;
    public String formatted_phone_number;

    public String[] types;

    public static class Geometry {
        public Location location;

        @Override
        public String toString() {
            return "Geometry{" +
                    "location=" + location +
                    '}';
        }
    }

    public static class Location {
        public double lat;
        public double lng;

        @Override
        public String toString() {
            return "Location{" +
                    "lat=" + lat +
                    ", lng=" + lng +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GooglePlaceDetails{" +
                "place_id='" + place_id + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", geometry=" + geometry +
                ", formatted_address='" + formatted_address + '\'' +
                ", formatted_phone_number='" + formatted_phone_number + '\'' +
                ", types=" + Arrays.toString(types) +
                '}';
    }
}
