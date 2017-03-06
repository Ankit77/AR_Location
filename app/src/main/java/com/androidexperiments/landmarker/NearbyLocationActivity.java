package com.androidexperiments.landmarker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.creativelabs.androidexperiments.typecompass.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Param;
import se.walkercrou.places.Place;

/**
 * Created by ANKIT on 2/26/2017.
 */

public class NearbyLocationActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private MapView mapView;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private AsyncLoadNearbyPlace asyncLoadNearbyPlace;
    private GooglePlaces mPlacesApi;
    private static final String PLACES_API_KEY = "AIzaSyAGvJJTXVY_OGyqlbiiFU9fUXIMG5hAu8I";
    private static final double MAX_RADIUS = 1000;
    private String placeCategory;

    private void buildPlacesApi() {
        mPlacesApi = new GooglePlaces(PLACES_API_KEY);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        if (getIntent().getExtras() != null) {
            placeCategory = getIntent().getExtras().getString("CATEGORY");
        }
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(NearbyLocationActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        buildPlacesApi();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng currentlocation = null;
        if (mLastLocation != null) {
            currentlocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlocation, 15));

        map.addMarker(new MarkerOptions()
                .title("MyLocation")
                .position(currentlocation));

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        mapView.getMapAsync(this);
        asyncLoadNearbyPlace = new AsyncLoadNearbyPlace();
        asyncLoadNearbyPlace.execute();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    private class AsyncLoadNearbyPlace extends AsyncTask<Void, Void, List<Place>> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NearbyLocationActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setTitle(getString(R.string.app_name));
            progressDialog.setMessage("Loading nearby place");
            progressDialog.show();
        }

        @Override
        protected List<Place> doInBackground(Void... params) {
            List<Place> places = null;
            Param param = new Param("type");
            try {
                param.value(URLEncoder.encode(placeCategory, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                places = mPlacesApi.getNearbyPlaces(mLastLocation.getLatitude(), mLastLocation.getLongitude(), MAX_RADIUS, 60, param);
            } catch (Exception e) {
                //if getNearbyPlaces fails, return null and directional will do what it needs to
                e.printStackTrace();
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            super.onPostExecute(places);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                if (places != null && places.size() > 0) {
                    LandmarkerApplication.getmInstance().setPlaces(places);
                    for (int i = 0; i < places.size(); i++) {
                        LatLng sydney = new LatLng(places.get(i).getLatitude(), places.get(i).getLongitude());
                        Marker marker = map.addMarker(new MarkerOptions()
                                .title(places.get(i).getName())
                                .position(sydney));

                        Projection projection = map.getProjection();
                        LatLng markerLocation = marker.getPosition();
                        Point screenPosition = projection.toScreenLocation(markerLocation);
                        Log.e("Position", "position - " + screenPosition.x);
                        Log.e("Position", "position - " + screenPosition.y);
                        LandmarkerApplication.getmInstance().getPlaceXY().add(screenPosition);
                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(NearbyLocationActivity.this, MainActivity.class));
                    }
                }, 5000);

            }
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
