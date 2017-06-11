package com.ahmednts.coformatiqueassignment.nearbyplaces;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ahmednts.coformatiqueassignment.R;
import com.ahmednts.coformatiqueassignment.data.UnifiedPlaceDetails;
import com.ahmednts.coformatiqueassignment.utils.UIUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearbyPlacesActivity extends AppCompatActivity implements NearbyPlacesContract.View, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    public static final String EXTRA_LIST = "EXTRA_LIST";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private NearbyPlacesContract.Presenter nearbyPlacesPresenter;

    @BindView(R.id.errorMessage)
    TextView errorMessage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.listView)
    RecyclerView listView;

    private PlacesAdapter placesAdapter;

    private SupportMapFragment mapView;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);
        ButterKnife.bind(this);

        initUI();

        nearbyPlacesPresenter = new NearbyPlacesPresenter(this);
        nearbyPlacesPresenter.loadNearbyPlacesList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        nearbyPlacesPresenter.stop();
    }

    void initUI() {

        UIUtils.setProgressBarColor(this, progressBar, R.color.colorAccent);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(layoutManager);
        placesAdapter = new PlacesAdapter(new ArrayList<>(0), itemClickListener);
        listView.setAdapter(placesAdapter);
    }

    PlacesAdapter.ItemClickListener itemClickListener = unifiedPlaceDetails -> {

    };

    @Override
    public void showListView(List<UnifiedPlaceDetails> unifiedPlaceDetailsList) {
        if (getSupportFragmentManager().findFragmentByTag("MapView") != null)
            getSupportFragmentManager().beginTransaction().remove(mapView).commit();

        listView.setVisibility(View.VISIBLE);
        placesAdapter.replaceData(unifiedPlaceDetailsList);
    }

    @Override
    public void showMapView() {
        listView.setVisibility(View.GONE);

        if (mapView == null || !mapView.isAdded()) {
            getSupportFragmentManager().beginTransaction().add(R.id.mapView, mapView = SupportMapFragment.newInstance(), "MapView").commit();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void showMapViewMarkers(List<UnifiedPlaceDetails> unifiedPlaceDetailsList) {
        mMap.clear();
        if (unifiedPlaceDetailsList != null && unifiedPlaceDetailsList.size() > 0) {

            for (UnifiedPlaceDetails unifiedPlaceDetails : unifiedPlaceDetailsList) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(unifiedPlaceDetails.getLat(), unifiedPlaceDetails.getLng()))
                        .title(unifiedPlaceDetails.getName()));
                marker.setTag(unifiedPlaceDetails);
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(unifiedPlaceDetailsList.get(0).getLat()
                    , unifiedPlaceDetailsList.get(0).getLng()), 12));
            mMap.setOnMarkerClickListener(this);
        }
    }

    @Override
    public void openDetailsUI(UnifiedPlaceDetails unifiedPlaceDetails) {

    }

    @Override
    public void showIndicator() {
        listView.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideIndicator() {
        progressBar.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
    }

    @Override
    public void showNoResultMessage() {
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText("There are no results");
    }

    @Override
    public void showNoNetworkMessage() {
        Snackbar.make(findViewById(android.R.id.content), "Please check your internet connection!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_switch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().equals("Map View")) {
            item.setTitle("List View");
            nearbyPlacesPresenter.switchToMapView();
        } else if (item.getTitle().equals("List View")) {
            item.setTitle("Map View");
            nearbyPlacesPresenter.switchToListView();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        nearbyPlacesPresenter.mapViewReady();

        enableMyLocation();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        UnifiedPlaceDetails unifiedPlaceDetails = (UnifiedPlaceDetails) marker.getTag();

        return false;
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        }
    }

    public static boolean isPermissionGranted(String[] grantPermissions, int[] grantResults, String permission) {
        for (int i = 0; i < grantPermissions.length; i++) {
            if (permission.equals(grantPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }

}
