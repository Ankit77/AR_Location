package com.androidexperiments.landmarker.webservice;

import android.content.Context;
import android.text.TextUtils;

import com.androidexperiments.landmarker.model.placedetail.PlaceDetailModel;

/**
 * Created by indianic on 11/03/17.
 */

public class WSGetPlaceDetail extends WebService {

    private Context context;

    public WSGetPlaceDetail(Context context) {
        super(context);
        this.context = context;
    }


    public PlaceDetailModel executeWebservice(String PlaceId, String APIKEY) {
        final String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + PlaceId + "&key=" + APIKEY;
        return parseJSONResponse(GET(url));

    }


    public PlaceDetailModel parseJSONResponse(final String response) {
        if (!TextUtils.isEmpty(response)) {
            PlaceDetailModel placeDetailModel = getGsonInstance().fromJson(response, PlaceDetailModel.class);
            return placeDetailModel;
        } else {
            return null;
        }

    }

}
