package com.ahmednts.coformatiqueassignment.data;

/**
 * Created by AhmedNTS on 6/11/2017.
 */

public class UnifiedPlaceDetails {
    private String id;
    private String name;

    private double rating;

    private String address;
    private String phone_number;

    private double lat;
    private double lng;

    public UnifiedPlaceDetails(String id, String name, double rating, String address, String phone_number, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.address = address;
        this.phone_number = phone_number;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
