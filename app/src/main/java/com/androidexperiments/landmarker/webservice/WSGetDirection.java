package com.androidexperiments.landmarker.webservice;

import android.content.Context;
import android.text.TextUtils;

import com.androidexperiments.landmarker.model.direction.DirectionModel;

/**
 * Created by indianic on 11/03/17.
 */

public class WSGetDirection extends WebService {

    private Context context;

    public WSGetDirection(Context context) {
        super(context);
        this.context = context;
    }


    public DirectionModel executeWebservice(String slat, String slan, String elat, String eLan, String APIKEY) {
        final String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + slat + "," + slan + "&destination=" + elat + "," + eLan + "&key=" + APIKEY;
        return parseJSONResponse(GET(url));

    }


    public DirectionModel parseJSONResponse(final String response) {
        if (!TextUtils.isEmpty(response)) {
            DirectionModel directionModel = getGsonInstance().fromJson(response, DirectionModel.class);
            return directionModel;
        } else {
            return null;
        }

    }
}
