package com.androidexperiments.landmarker;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.androidexperiments.landmarker.service.LocationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.creativelabs.androidexperiments.typecompass.R;

/**
 * Splash shit
 */
public class SplashActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_splash);
        buildLocation();

    }

    private void buildLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

//                Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                        mGoogleApiClient);
//                if (mLastLocation != null) {
//                    LandmarkerApplication.getmInstance().setCurrentLocation(mLastLocation);
//                }
                // NO need to show the dialog;
                runSplash();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(SplashActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {
//                Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                        mGoogleApiClient);
//                if (mLastLocation != null) {
//                    LandmarkerApplication.getmInstance().setCurrentLocation(mLastLocation);
//                }
                runSplash();
            }

        }
    }

    private void runSplash() {
        Intent intent = new Intent(SplashActivity.this, LocationService.class);
        startService(intent);
        new Handler().postDelayed(new Runnable() {

			/*
             * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, PlaceCategoryActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }, 3000);
    }

}
