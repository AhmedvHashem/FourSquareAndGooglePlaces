package com.ahmednts.coformatiqueassignment.data;

/**
 * Created by AhmedNTS on 6/11/2017.
 */

public class UnifiedPlaceDetails {
    public String id;
    public String name;

    public double rating;

    public String address;
    public String phone_number;

    public double lat;
    public double lng;

    public UnifiedPlaceDetails(String id, String name, double rating, String address, String phone_number, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.address = address;
        this.phone_number = phone_number;
        this.lat = lat;
        this.lng = lng;
    }
}
