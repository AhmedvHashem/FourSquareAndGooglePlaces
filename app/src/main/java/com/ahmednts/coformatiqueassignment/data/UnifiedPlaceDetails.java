package com.ahmednts.coformatiqueassignment.data;

/**
 * Created by AhmedNTS on 6/11/2017.
 */

public class UnifiedPlaceDetails {

    private ApiType apiType;

    private String id;
    private String name;

    private double rating;

    private String address;
    private String phone_number;

    private double lat;
    private double lng;

    private String category;

    public static enum ApiType {
        GOOGLE,
        FOURESQUARE
    }

    public UnifiedPlaceDetails(ApiType apiType, String id, String name, double rating, String address, String phone_number, double lat, double lng, String category) {
        this.apiType = apiType;
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.address = address;
        this.phone_number = phone_number;
        this.lat = lat;
        this.lng = lng;
        this.category = category;
    }

    public ApiType getApiType() {
        return apiType;
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

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "UnifiedPlaceDetails{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", address='" + address + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", category='" + category + '\'' +
                '}';
    }
}
