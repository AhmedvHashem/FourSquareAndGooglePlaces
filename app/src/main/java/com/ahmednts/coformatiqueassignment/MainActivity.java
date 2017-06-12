package com.ahmednts.coformatiqueassignment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.ahmednts.coformatiqueassignment.nearbyplaces.NearbyPlacesActivity;
import com.ahmednts.coformatiqueassignment.utils.LocationGPS;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERM_LOCATION = 2000;

    public MainActivity getMyActivity() {
        return this;
    }

    public Context getMyContext() {
        return this;
    }

    public Context getMyBaseContext() {
        return getBaseContext();
    }

    public Context getMyAppContext() {
        return getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsAlertDialog();
            return;
        }
        RequestLocationGPSPermissions();
    }


    @Override
    protected void onStart() {
        super.onStart();

        int errorCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getMyContext());
        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(this, errorCode, 1000);
            if (errorDialog != null)
                errorDialog.show();
        }
    }

    @Override
    protected void onStop() {
        if (locationGPS != null) {
            locationGPS.Disconnect();
            locationGPS = null;
        }
        super.onStop();
    }

    private void RequestLocationGPSPermissions() {
        String[] locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, locationPermissions, REQUEST_CODE_PERM_LOCATION);
            } else
                RequestLocationService();
        } else
            RequestLocationService();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERM_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    RequestLocationService();
                }
                break;
            }
        }
    }

    LocationGPS locationGPS;

    private void RequestLocationService() {
        ShowProgressDialog("Getting location from GPS...");

        if (locationGPS == null)
            locationGPS = new LocationGPS(this, new LocationGPS.LocationGPSListener() {
                @Override
                public void OnLocationReceived(@NonNull LocationGPS.CurrentAddress address) {
//                    Log.d(TAG, address.toString());

                    HideProgressDialog();
                    App.getInstance().location = address;

                    Intent intent = new Intent(getMyContext(), NearbyPlacesActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        locationGPS.Connect();
    }

    public ProgressDialog progress;

    public void ShowProgressDialog(String msg) {
        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
        progress.setMessage(msg);
        if (!progress.isShowing())
            progress.show();
    }

    public void HideProgressDialog() {
        if (progress != null)
            progress.dismiss();
    }

    public void gpsAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getMyContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                finish();
            }
        });

        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

}
