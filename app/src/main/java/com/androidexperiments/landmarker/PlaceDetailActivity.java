package com.androidexperiments.landmarker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidexperiments.landmarker.adapter.ImagesAdapter;
import com.androidexperiments.landmarker.adapter.ReviewAdapter;
import com.androidexperiments.landmarker.adapter.WorkingHourAdapter;
import com.androidexperiments.landmarker.model.placedetail.Photo;
import com.androidexperiments.landmarker.model.placedetail.PlaceDetailModel;
import com.androidexperiments.landmarker.model.placedetail.Review;
import com.androidexperiments.landmarker.util.Const;
import com.androidexperiments.landmarker.util.Utils;
import com.androidexperiments.landmarker.webservice.WSGetPlaceDetail;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.creativelabs.androidexperiments.typecompass.R;

import java.util.List;

/**
 * Created by indianic on 11/03/17.
 */

public class PlaceDetailActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private Place placeInfo;
    private String placeId;
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
    private String distance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeinfo);
        mapView = (MapView) findViewById(R.id.activity_placeinfo_mapview);
        mapView.onCreate(savedInstanceState);
        if (Utils.isNetworkAvailable(PlaceDetailActivity.this)) {
            asyncLoadPlaceDetail = new AsyncLoadPlaceDetail();
            asyncLoadPlaceDetail.execute("ChIJs-eWozubXjkRiux63_B6rbE");
        } else {

        }
    }

    private void init() {
        llcall = (LinearLayout) findViewById(R.id.activity_placeinfo_ll_call);
        llmap = (LinearLayout) findViewById(R.id.activity_placeinfo_ll_map);
        llWebsite = (LinearLayout) findViewById(R.id.activity_placeinfo_ll_website);
        tvPlaceFullName = (TextView) findViewById(R.id.activity_placeinfo_img_placename);
        rbRate = (RatingBar) findViewById(R.id.activity_placeinfo_rb_rating);
        tvFullAddress = (TextView) findViewById(R.id.activity_placeinfo_tv_addressfull);
        tvDistance = (TextView) findViewById(R.id.activity_placeinfo_tv_distance);
        tvCall = (TextView) findViewById(R.id.activity_placeinfo_tv_call);
        tvWebsite = (TextView) findViewById(R.id.activity_placeinfo_tv_websitefull);
        tvWorkingNow = (TextView) findViewById(R.id.activity_placeinfo_tv_workingnow);
        rvImages = (RecyclerView) findViewById(R.id.activity_placeinfo_rv_images);
        rvWorkingHour = (RecyclerView) findViewById(R.id.activity_placeinfo_rv_working);
        rvReviews = (RecyclerView) findViewById(R.id.activity_placeinfo_rv_reviews);

        mapView.getMapAsync(this);
        llcall.setOnClickListener(this);
        llWebsite.setOnClickListener(this);
        llmap.setOnClickListener(this);
    }

    private void loadData(PlaceDetailModel placeDetailModel) {

        tvPlaceFullName.setText(placeDetailModel.getResult().getName());
        rbRate.setRating(Float.parseFloat(placeDetailModel.getResult().getRating().toString()));
        tvFullAddress.setText(placeDetailModel.getResult().getFormattedAddress());
        tvDistance.setText("Distance : - " + distance);
        tvCall.setText(placeDetailModel.getResult().getInternationalPhoneNumber());
        tvWebsite.setText(placeDetailModel.getResult().getWebsite());
    }

    private void loadPlacePhoto(List<Photo> photoList) {
        imagesAdapter = new ImagesAdapter(PlaceDetailActivity.this, photoList);
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

        } else if (view == llmap) {

        } else if (view == llWebsite) {

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
//        map.addMarker(new MarkerOptions()
//                .title("MyLocation")
//                .position(currentlocation));
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

    private class AsyncLoadPlaceDetail extends AsyncTask<String, Void, PlaceDetailModel> {
        private ProgressDialog progressDialog;
        private WSGetPlaceDetail wsGetPlaceDetail;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Utils.displayProgressDialog(PlaceDetailActivity.this);
        }

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
                if (placeDetailModel != null) {
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
                        loadWorkingHours(placeDetailModel.getResult().getOpeningHours().getWeekdayText());
                    }

                    if (placeDetailModel.getResult() != null && placeDetailModel.getResult().getReviews() != null) {

                        loadPReviews(placeDetailModel.getResult().getReviews());
                    }
                }
            }
        }
    }
}
