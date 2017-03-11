package com.androidexperiments.landmarker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.androidexperiments.landmarker.model.placedetail.PlaceDetailModel;
import com.androidexperiments.landmarker.util.Const;
import com.androidexperiments.landmarker.util.Utils;
import com.androidexperiments.landmarker.webservice.WSGetPlaceDetail;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;

/**
 * Created by indianic on 11/03/17.
 */

public class PlaceDetailActivity extends AppCompatActivity {
    private Place placeInfo;
    private String placeId;
    private GoogleApiClient mGoogleApiClient;
    private AsyncLoadPlaceDetail asyncLoadPlaceDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utils.isNetworkAvailable(PlaceDetailActivity.this)) {
            asyncLoadPlaceDetail = new AsyncLoadPlaceDetail();
            asyncLoadPlaceDetail.execute("ChIJs-eWozubXjkRiux63_B6rbE");
        } else {

        }
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

            }
        }
    }
}
