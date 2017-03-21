package com.androidexperiments.landmarker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidexperiments.landmarker.adapter.ImagesAdapter;
import com.androidexperiments.landmarker.adapter.ReviewAdapter;
import com.androidexperiments.landmarker.adapter.WorkingHourAdapter;
import com.androidexperiments.landmarker.model.direction.DirectionModel;
import com.androidexperiments.landmarker.model.placedetail.Photo;
import com.androidexperiments.landmarker.model.placedetail.PlaceDetailModel;
import com.androidexperiments.landmarker.model.placedetail.Review;
import com.androidexperiments.landmarker.util.Const;
import com.androidexperiments.landmarker.util.Utils;
import com.androidexperiments.landmarker.webservice.WSGetDirection;
import com.androidexperiments.landmarker.webservice.WSGetPlaceDetail;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.creativelabs.androidexperiments.typecompass.R;

import java.util.List;

/**
 * Created by indianic on 11/03/17.
 */

public class PlaceDetailActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, ImagesAdapter.ImageClickListner {
    private AsyncLoadPlaceDetail asyncLoadPlaceDetail;
    private LinearLayout llmap;
    private LinearLayout llcall;
    private LinearLayout llWebsite;
    private TextView tvPlaceFullName;
    private GoogleMap map;
    private MapView mapView;
    private RatingBar rbRate;
    private TextView tvFullAddress;
    private TextView tvDistance;
    private TextView tvCall;
    private TextView tvWebsite;
    private TextView tvWorkingNow;
    private RecyclerView rvImages;
    private RecyclerView rvWorkingHour;
    private RecyclerView rvReviews;
    private ImagesAdapter imagesAdapter;
    private ReviewAdapter reviewAdapter;
    private WorkingHourAdapter workingHourAdapter;
    private AsyncGetDiretion asyncGetDiretion;
    private String distance;
    private String duration;
    private LatLng startLocation;
    private LatLng endLocation;
    private ProgressDialog progressDialog;
    private Double endLat;
    private Double endLan;
    private Toolbar toolbar;
    private ImageView imgBack;
    private TextView tvTitle;
    private PlaceDetailModel mPlaceDetailModel;
    private String placeId;
    private String placeName;
    private TextView tvLabelWorkingHours;
    private LinearLayout llWorkingHours;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeinfo);
        if (getIntent().getExtras() != null) {
            placeId = getIntent().getExtras().getString(Const.KEY_PLACEID, "");
            placeName = getIntent().getExtras().getString(Const.KEY_PLACENAME, "");
            endLat = getIntent().getExtras().getDouble(Const.KEY_LAT, 0.0);
            endLan = getIntent().getExtras().getDouble(Const.KEY_LAN, 0.0);
        }
        mapView = (MapView) findViewById(R.id.activity_placeinfo_mapview);
        mapView.onCreate(savedInstanceState);
        if (LandmarkerApplication.getmInstance().getCurrentLocation() != null) {
            init();
        } else {
            Utils.displayDialog(PlaceDetailActivity.this, getString(R.string.app_name), getString(R.string.alert_empty_current_location));
        }

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        llcall = (LinearLayout) findViewById(R.id.activity_placeinfo_ll_call);
        llmap = (LinearLayout) findViewById(R.id.activity_placeinfo_ll_map);
        llWebsite = (LinearLayout) findViewById(R.id.activity_placeinfo_ll_website);
        tvPlaceFullName = (TextView) findViewById(R.id.activity_placeinfo_tv_placename);
        rbRate = (RatingBar) findViewById(R.id.activity_placeinfo_rb_rating);
        tvFullAddress = (TextView) findViewById(R.id.activity_placeinfo_tv_addressfull);
        tvDistance = (TextView) findViewById(R.id.activity_placeinfo_tv_distance);
        tvCall = (TextView) findViewById(R.id.activity_placeinfo_tv_call);
        tvWebsite = (TextView) findViewById(R.id.activity_placeinfo_tv_websitefull);
        tvWorkingNow = (TextView) findViewById(R.id.activity_placeinfo_tv_workingnow);
        rvImages = (RecyclerView) findViewById(R.id.activity_placeinfo_rv_images);
        rvWorkingHour = (RecyclerView) findViewById(R.id.activity_placeinfo_rv_working);
        rvReviews = (RecyclerView) findViewById(R.id.activity_placeinfo_rv_reviews);
        imgBack = (ImageView) findViewById(R.id.activity_placeinfo_img_back);
        tvTitle = (TextView) findViewById(R.id.activity_placeinfo_tv_title);
        tvLabelWorkingHours = (TextView) findViewById(R.id.activity_placeinfo_tv_lblworkinghr);
        llWorkingHours = (LinearLayout) findViewById(R.id.activity_placeinfo_ll_workinghr);

        mapView.getMapAsync(this);
        llcall.setOnClickListener(this);
        llWebsite.setOnClickListener(this);
        llmap.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        rvImages.setNestedScrollingEnabled(false);
        rvWorkingHour.setNestedScrollingEnabled(false);
        rvReviews.setNestedScrollingEnabled(false);
        if (Utils.isNetworkAvailable(PlaceDetailActivity.this)) {
            asyncGetDiretion = new AsyncGetDiretion();
            asyncGetDiretion.execute(String.valueOf(LandmarkerApplication.getmInstance().getCurrentLocation().getLatitude()), String.valueOf(LandmarkerApplication.getmInstance().getCurrentLocation().getLongitude()), String.valueOf(endLat), String.valueOf(endLan));
        } else {
            Utils.displayDialog(PlaceDetailActivity.this, getString(R.string.app_name), getString(R.string.alert_internet_connectivity));
        }
    }

    private void loadData(PlaceDetailModel placeDetailModel) {
        LatLng latLng = new LatLng(endLat, endLan);
        if (map != null) {
            Marker marker = map.addMarker(new MarkerOptions()
                    .title(placeDetailModel.getResult().getName())
                    .position(latLng));
            marker.showInfoWindow();
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20.0f));
        }
        if (TextUtils.isEmpty(placeDetailModel.getResult().getInternationalPhoneNumber())) {
            llcall.setVisibility(View.GONE);
        } else {
            tvCall.setText(placeDetailModel.getResult().getInternationalPhoneNumber());
            llcall.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(placeDetailModel.getResult().getWebsite())) {
            llWebsite.setVisibility(View.GONE);
        } else {
            tvWebsite.setText(placeDetailModel.getResult().getWebsite());
            llWebsite.setVisibility(View.VISIBLE);
        }

        tvTitle.setText(placeDetailModel.getResult().getName());
        tvPlaceFullName.setText(placeDetailModel.getResult().getName());
        if (placeDetailModel.getResult().getRating() != null) {
            rbRate.setRating(Float.parseFloat(placeDetailModel.getResult().getRating().toString()));
        } else {
            rbRate.setRating(0.0f);
        }
        tvFullAddress.setText(placeDetailModel.getResult().getFormattedAddress());
        tvDistance.setText("Distance : - " + distance);


    }

    private void loadPlacePhoto(List<Photo> photoList) {
        LandmarkerApplication.getmInstance().setPlacesphotos(photoList);
        imagesAdapter = new ImagesAdapter(PlaceDetailActivity.this, photoList);
        imagesAdapter.setImageClickListner(this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvImages.setLayoutManager(layoutManager);
        rvImages.setItemAnimator(new DefaultItemAnimator());
        rvImages.setAdapter(imagesAdapter);
    }

    private void loadPReviews(List<Review> reviewList) {
        reviewAdapter = new ReviewAdapter(PlaceDetailActivity.this, reviewList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvReviews.setLayoutManager(mLayoutManager);
        rvReviews.setItemAnimator(new DefaultItemAnimator());
        rvReviews.setAdapter(reviewAdapter);
    }

    private void loadWorkingHours(List<String> workinghourList) {
        workingHourAdapter = new WorkingHourAdapter(PlaceDetailActivity.this, workinghourList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvWorkingHour.setLayoutManager(mLayoutManager);
        rvWorkingHour.setItemAnimator(new DefaultItemAnimator());
        rvWorkingHour.setAdapter(workingHourAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view == llcall) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + mPlaceDetailModel.getResult().getInternationalPhoneNumber()));//change the number
            startActivity(callIntent);
        } else if (view == llmap) {
            Intent intent = new Intent(PlaceDetailActivity.this, PlaceDirectionActivity.class);
            intent.putExtra(Const.KEY_SLAT, LandmarkerApplication.getmInstance().getCurrentLocation().getLatitude());
            intent.putExtra(Const.KEY_SLAN, LandmarkerApplication.getmInstance().getCurrentLocation().getLongitude());
            intent.putExtra(Const.KEY_ELAT, endLat);
            intent.putExtra(Const.KEY_ELAN, endLan);
            intent.putExtra(Const.KEY_PLACENAME, mPlaceDetailModel.getResult().getName());
            intent.putExtra(Const.KEY_TITLE, mPlaceDetailModel.getResult().getName());
            startActivity(intent);
        } else if (view == llWebsite) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPlaceDetailModel.getResult().getWebsite()));
            startActivity(browserIntent);
        } else if (view == imgBack) {
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
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

    @Override
    public void onImageClick(int position) {
        Intent intent = new Intent(PlaceDetailActivity.this, PlaceImageActivity.class);
        intent.putExtra(Const.KEY_TITLE, mPlaceDetailModel.getResult().getName());
        startActivity(intent);
    }

    private class AsyncLoadPlaceDetail extends AsyncTask<String, Void, PlaceDetailModel> {
        private WSGetPlaceDetail wsGetPlaceDetail;


        @Override
        protected PlaceDetailModel doInBackground(String... strings) {
            String placeid = strings[0];
            wsGetPlaceDetail = new WSGetPlaceDetail(PlaceDetailActivity.this);
            return wsGetPlaceDetail.executeWebservice(placeid, Const.PLACES_API_KEY);
        }

        @Override
        protected void onPostExecute(PlaceDetailModel placeDetailModel) {
            super.onPostExecute(placeDetailModel);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (placeDetailModel != null) {
                mPlaceDetailModel = placeDetailModel;
                loadData(placeDetailModel);
                //Load Place Photo
                if (placeDetailModel.getResult() != null && placeDetailModel.getResult().getPhotos() != null) {
                    loadPlacePhoto(placeDetailModel.getResult().getPhotos());
                }
                //Load Place Working hours
                if (placeDetailModel.getResult() != null && placeDetailModel.getResult().getOpeningHours() != null && placeDetailModel.getResult().getOpeningHours().getWeekdayText() != null) {
                    if (placeDetailModel.getResult().getOpeningHours().getOpenNow()) {
                        tvWorkingNow.setText(R.string.alert_open_now);
                    } else {
                        tvWorkingNow.setText(R.string.alert_close_now);
                    }
                    tvLabelWorkingHours.setVisibility(View.VISIBLE);
                    llWorkingHours.setVisibility(View.VISIBLE);
                    loadWorkingHours(placeDetailModel.getResult().getOpeningHours().getWeekdayText());
                } else {
                    tvLabelWorkingHours.setVisibility(View.GONE);
                    llWorkingHours.setVisibility(View.GONE);
                }

                if (placeDetailModel.getResult() != null && placeDetailModel.getResult().getReviews() != null) {
                    loadPReviews(placeDetailModel.getResult().getReviews());
                }
            }
        }

    }


    private class AsyncGetDiretion extends AsyncTask<String, Void, DirectionModel> {

        private WSGetDirection wsGetDirection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Utils.displayProgressDialog(PlaceDetailActivity.this);
        }

        @Override
        protected DirectionModel doInBackground(String... strings) {
            String slat = strings[0];
            String slan = strings[1];
            String elat = strings[2];
            String elan = strings[3];
            wsGetDirection = new WSGetDirection(PlaceDetailActivity.this);
            return wsGetDirection.executeWebservice(slat, slan, elat, elan, Const.PLACES_API_KEY);
        }

        @Override
        protected void onPostExecute(DirectionModel directionModel) {
            super.onPostExecute(directionModel);

            if (directionModel.getStatus().equalsIgnoreCase("OK")) {
                LandmarkerApplication.getmInstance().setDirectionModel(directionModel);
                distance = directionModel.getRoutes().get(0).getLegs().get(0).getDistance().getText();
                duration = directionModel.getRoutes().get(0).getLegs().get(0).getDuration().getText();
                endLocation = new LatLng(directionModel.getRoutes().get(0).getLegs().get(0).getEndLocation().getLat(), directionModel.getRoutes().get(0).getLegs().get(0).getEndLocation().getLng());
                startLocation = new LatLng(directionModel.getRoutes().get(0).getLegs().get(0).getStartLocation().getLat(), directionModel.getRoutes().get(0).getLegs().get(0).getStartLocation().getLng());
            }

            asyncLoadPlaceDetail = new AsyncLoadPlaceDetail();
            asyncLoadPlaceDetail.execute(placeId);
        }
    }

}
