package com.ahmednts.coformatiqueassignment.data.fouresquare;

import java.util.Arrays;

/**
 * Created by AhmedNTS on 6/11/2017.
 */

public class FoursquarePlaceDetails {

    public String id;
    public String name;

    public double rating;

    public Location location;

    public Contact contact;

    public Category[] categories;

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

    public static class Contact {
        public String formattedPhone;

        @Override
        public String toString() {
            return "Contact{" +
                    "formattedPhone='" + formattedPhone + '\'' +
                    '}';
        }
    }

    public static class Category{
        public String id;
        public String name;

        @Override
        public String toString() {
            return "Category{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FoursquarePlaceDetails{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", location=" + location +
                ", contact=" + contact +
                ", categories=" + Arrays.toString(categories) +
                '}';
    }
}
