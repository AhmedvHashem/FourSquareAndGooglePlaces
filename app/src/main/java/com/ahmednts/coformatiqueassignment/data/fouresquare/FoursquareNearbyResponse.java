package com.ahmednts.coformatiqueassignment.data.fouresquare;

import java.util.List;

/**
 * Created by AhmedNTS on 6/11/2017.
 */
public class FoursquareNearbyResponse {

    public Meta meta;

    public Response response;

    public static class Meta {
        public int code;

        @Override
        public String toString() {
            return "Meta{" +
                    "code=" + code +
                    '}';
        }
    }

    public static class Response {
        public List<Group> groups;

        @Override
        public String toString() {
            return "Response{" +
                    "groups=" + groups +
                    '}';
        }
    }

    public static class Group {
        public List<Item> items;

        public static class Item {
            public FoursquarePlaceDetails venue;

            @Override
            public String toString() {
                return "Item{" +
                        "venue=" + venue +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Group{" +
                    "items=" + items +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FoursquareNearbyResponse{" +
                "meta=" + meta +
                ", response=" + response +
                '}';
    }
}
