package com.ahmednts.coformatiqueassignment.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.widget.ProgressBar;

/**
 * Created by AhmedNTS on 6/2/2017.
 */
public class Utils {
    public static void setProgressBarColor(Context context, ProgressBar progressBar, int color) {
        progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(color), PorterDuff.Mode.SRC_IN);
    }

    public static void CallPhone(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
    }

    public static void OpenDirections(Context context, double lat, double lng) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + lat + "," + lng));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
    }
}
