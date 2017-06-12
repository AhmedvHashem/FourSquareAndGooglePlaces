package com.ahmednts.coformatiqueassignment.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.Serializable;
import java.lang.ref.WeakReference;

/*
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
{
	switch (requestCode)
	{
		case 2000:
		{
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
			{
				locationGPS.Connect();
			}
			else
			{
				// permission denied, boo! Disable the
				// functionality that depends on this permission.
			}
			break;
		}
	}
}
*/
public class LocationGPS
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final static String PREF_LOCATION = "PREF_LOCATION";

    private WeakReference<Activity> mActivity;
    private LocationGPSListener mListener;

    private GoogleApiClient mGoogleApiClient;

    public LocationGPS(Activity activity, LocationGPSListener listener) {
        this.mActivity = new WeakReference<>(activity);
        this.mListener = listener;
    }

    public void Connect() {
        LocationManager locationManager = (LocationManager) mActivity.get().getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return;
        }
        if (mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient.Builder(mActivity.get())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApiIfAvailable(LocationServices.API)
                    .build();

        Disconnect();
        mGoogleApiClient.connect();
    }

    public void Disconnect() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) throws SecurityException {

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (lastLocation != null) {
            CurrentAddress currentAddress = new CurrentAddress(lastLocation.getLatitude(), lastLocation.getLongitude());
            setStoredAddress(currentAddress);
            mActivity.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null)
                        mListener.OnLocationReceived(currentAddress);
                }
            });
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Connect();
                }
            }, 1000);
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.reconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e("LocationGPS", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());

        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.reconnect();
    }

    private void setStoredAddress(CurrentAddress currentAddress) {
        SharedPreferences.Editor sp = mActivity.get().getApplicationContext().getSharedPreferences(PREF_LOCATION, Activity.MODE_PRIVATE).edit();
        sp.putFloat("Latitude", (float) currentAddress.Latitude);
        sp.putFloat("Longitude", (float) currentAddress.Longitude);
        sp.putString("Country", currentAddress.Country);
        sp.putString("CountryCode", currentAddress.CountryCode);
        sp.putString("City", currentAddress.City);
        sp.putString("State", currentAddress.State);
        sp.putString("Street", currentAddress.Street);
        sp.apply();
    }

    private CurrentAddress getStoredAddress() {
        CurrentAddress ca = new CurrentAddress();
        SharedPreferences sp = mActivity.get().getApplicationContext().getSharedPreferences(PREF_LOCATION, Activity.MODE_PRIVATE);
        ca.Latitude = sp.getFloat("Latitude", 0);
        ca.Longitude = sp.getFloat("Longitude", 0);
        ca.Country = sp.getString("Country", "");
        ca.CountryCode = sp.getString("CountryCode", "");
        ca.City = sp.getString("City", "");
        ca.State = sp.getString("State", "");
        ca.Street = sp.getString("Street", "");
        return ca;
    }

    public interface LocationGPSListener {
        void OnLocationReceived(@NonNull CurrentAddress address);
    }

    public static class CurrentAddress implements Serializable {
        public String Street = "";
        public String State = "";
        public String City = "";
        public String Country = "";
        public String CountryCode = "";

        public double Latitude;
        public double Longitude;

        public CurrentAddress() {
        }

        public CurrentAddress(double latitude, double longitude) {
            Latitude = latitude;
            Longitude = longitude;
        }

        @Override
        public String toString() {
            return Latitude + "," + Longitude;
        }
    }
}
