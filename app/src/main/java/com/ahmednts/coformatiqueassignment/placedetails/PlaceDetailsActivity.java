package com.ahmednts.coformatiqueassignment.placedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ahmednts.coformatiqueassignment.R;
import com.ahmednts.coformatiqueassignment.data.UnifiedPlaceDetails;
import com.ahmednts.coformatiqueassignment.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class PlaceDetailsActivity extends AppCompatActivity implements PlaceDetailsContract.View {
    public static final String EXTRA_API_TYPE = "EXTRA_API_TYPE";
    public static final String EXTRA_ID = "EXTRA_ID";

    @BindView(R.id.placeContainer)
    View placeContainer;
    @BindView(R.id.placeName)
    TextView placeName;
    @BindView(R.id.placeCategory)
    TextView placeCategory;
    @BindView(R.id.placeRating)
    MaterialRatingBar placeRating;
    @BindView(R.id.placeNumber)
    TextView placeNumber;
    @BindView(R.id.placeAddress)
    TextView placeAddress;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private PlaceDetailsContract.Presenter placeDetailsPresenter;

    public static void open(Context context, int apiType, String id) {
        Intent intent = new Intent(context, PlaceDetailsActivity.class);
        intent.putExtra(EXTRA_API_TYPE, apiType);
        intent.putExtra(EXTRA_ID, id);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButterKnife.bind(this);

        initUI();

        int apiType = getIntent().getIntExtra(EXTRA_API_TYPE, 0);
        String id = getIntent().getStringExtra(EXTRA_ID);

        placeDetailsPresenter = new PlaceDetailsPresenter(this);
        placeDetailsPresenter.loadPlaceDetails(apiType, id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        placeDetailsPresenter.stop();
    }

    void initUI() {
        Utils.setProgressBarColor(this, progressBar, R.color.colorAccent);
    }

    @Override
    public void showPlaceDetails(UnifiedPlaceDetails unifiedPlaceDetails) {
        placeName.setText("Place Name: " + unifiedPlaceDetails.getName());
        placeCategory.setText("Place Category: " + unifiedPlaceDetails.getCategory());
        placeRating.setRating((float) unifiedPlaceDetails.getRating());
        placeNumber.setText("Place Number: " + (unifiedPlaceDetails.getPhone_number() == null ? "No Phone Number" : unifiedPlaceDetails.getPhone_number()));
        placeAddress.setText("Place Address: " + unifiedPlaceDetails.getAddress());
    }

    @Override
    public void showPhoneUI(String number) {
        if (number != null)
            Utils.CallPhone(this, number);
    }

    @Override
    public void showDirectionUI(double lat, double lng) {
        Utils.OpenDirections(this, lat, lng);
    }

    @Override
    public void showIndicator() {
        placeContainer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideIndicator() {
        progressBar.setVisibility(View.GONE);
        placeContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoNetworkMessage() {
        Snackbar.make(findViewById(android.R.id.content), "Please check your internet connection!", Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.placeNumber)
    void onNumberClicked() {
        placeDetailsPresenter.callPlaceNumber();
    }

    @OnClick(R.id.placeAddress)
    void onAddressClicked() {
        placeDetailsPresenter.openPlaceDirection();
    }
}
