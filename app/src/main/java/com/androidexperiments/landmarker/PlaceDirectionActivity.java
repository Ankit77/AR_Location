package com.androidexperiments.landmarker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidexperiments.landmarker.adapter.DirectionAdapter;
import com.androidexperiments.landmarker.model.direction.DirectionModel;
import com.androidexperiments.landmarker.model.direction.Leg;
import com.androidexperiments.landmarker.model.direction.Step;
import com.androidexperiments.landmarker.util.Const;
import com.androidexperiments.landmarker.util.Utils;
import com.androidexperiments.landmarker.webservice.WSGetDirection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.creativelabs.androidexperiments.typecompass.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by indianic on 11/03/17.
 */

public class PlaceDirectionActivity extends AppCompatActivity implements OnMapReadyCallback {
    private AsyncGetDiretion asyncGetDiretion;
    private MapView mapView;
    private GoogleMap map;
    private RecyclerView rvDirection;
    private DirectionAdapter diretionAdapter;
    private ArrayList<Step> stepList;
    private TextView tvDistance;
    private Double startLat;
    private Double startLan;
    private Double endLat;
    private Double endLan;
    private DirectionModel directionModel;
    private String placeName;
    private TextView tvTitle;
    private ImageView imgBack;
    private String title;
    private ImageView imgDirection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placedirection);

        tvTitle = (TextView) findViewById(R.id.activity_placedirection_tv_title);
        imgBack = (ImageView) findViewById(R.id.activity_placedirection_img_back);
        directionModel = LandmarkerApplication.getmInstance().getDirectionModel();
        if (getIntent().getExtras() != null) {
            startLat = getIntent().getExtras().getDouble(Const.KEY_SLAT);
            startLan = getIntent().getExtras().getDouble(Const.KEY_SLAN);
            endLat = getIntent().getExtras().getDouble(Const.KEY_ELAT);
            endLan = getIntent().getExtras().getDouble(Const.KEY_ELAN);
            placeName = getIntent().getExtras().getString(Const.KEY_PLACENAME);
            title = getIntent().getExtras().getString(Const.KEY_TITLE, "");
        }
        tvDistance = (TextView) findViewById(R.id.activity_placedirection_tv_totaldistance);
        rvDirection = (RecyclerView) findViewById(R.id.activity_placedirection_rvsteps);
        imgDirection = (ImageView) findViewById(R.id.activity_placedirection_img_direction);
        mapView = (MapView) findViewById(R.id.activity_placedirection_mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        tvTitle.setText(title);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + startLat + "," + startLan + "&daddr=" + endLat + "," + endLan));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

    }

    private void loadStep(ArrayList<Step> steplist) {
        diretionAdapter = new DirectionAdapter(PlaceDirectionActivity.this, steplist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PlaceDirectionActivity.this);
        rvDirection.setLayoutManager(mLayoutManager);
        rvDirection.setAdapter(diretionAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (directionModel != null) {
            if (directionModel.getStatus().equalsIgnoreCase("OK")) {
                if (map != null) {
                    LatLng latLng = new LatLng(endLat, endLan);
                    Marker marker = map.addMarker(new MarkerOptions()
                            .title(placeName)
                            .position(latLng));
                    marker.showInfoWindow();
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                }

                List<Step> steplist = directionModel.getRoutes().get(0).getLegs().get(0).getSteps();
                if (steplist != null && steplist.size() > 0) {
                    stepList = new ArrayList<>();
                    stepList.addAll(steplist);
                    loadStep(stepList);
                    Leg leg = directionModel.getRoutes().get(0).getLegs().get(0);
                    if (leg != null) {
                        LatLng src = new LatLng(leg.getStartLocation().getLat(), leg.getStartLocation().getLng());
                        LatLng des = new LatLng(leg.getEndLocation().getLat(), leg.getEndLocation().getLng());
                        drawPathOnMap(src, des, stepList);
                        tvDistance.setText(leg.getDistance().getText() + " , " + leg.getDuration().getText());
                    }
                }
            }
        }
    }

    private class AsyncGetDiretion extends AsyncTask<String, Void, DirectionModel> {
        private ProgressDialog progressDialog;
        private WSGetDirection wsGetDirection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Utils.displayProgressDialog(PlaceDirectionActivity.this);
        }

        @Override
        protected DirectionModel doInBackground(String... strings) {
            String slat = strings[0];
            String slan = strings[1];
            String elat = strings[0];
            String elan = strings[1];
            wsGetDirection = new WSGetDirection(PlaceDirectionActivity.this);
            return wsGetDirection.executeWebservice(slat, slan, elat, elan, Const.PLACES_API_KEY);
        }

        @Override
        protected void onPostExecute(DirectionModel directionModel) {
            super.onPostExecute(directionModel);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();

            }
        }
    }

    private void drawPathOnMap(LatLng source, LatLng des, ArrayList<Step> steps) {
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        options.add(source);
        for (int z = 0; z < steps.size(); z++) {
            LatLng point = new LatLng(steps.get(z).getStartLocation().getLat(), steps.get(z).getStartLocation().getLng());
            options.add(point);
            LatLng point1 = new LatLng(steps.get(z).getEndLocation().getLat(), steps.get(z).getEndLocation().getLng());
            options.add(point1);
        }
        options.add(des);
        map.addPolyline(options);
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
