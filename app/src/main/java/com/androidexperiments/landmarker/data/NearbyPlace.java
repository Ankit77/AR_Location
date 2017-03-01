package com.androidexperiments.landmarker.data;

import android.location.Location;

import java.util.List;

/**
 * Nearby place with the name (used for maps and display) and the
 * distance to that place, calculated against our current {@link Location}
 *
 * @see com.androidexperiments.landmarker.widget.DirectionalTextViewContainer#updatePlaces(List, Location)
 */
public class NearbyPlace
{
    private float distance;
    private String name;
    private double lat;
    private double lang;
    private String placeurl;

    public NearbyPlace(float distance, String name,double lat,double lang,String placeurl) {
        this.distance = distance;
        this.name = name;
        this.lat=lat;
        this.lang=lang;
        this.placeurl=placeurl;
    }

    public float getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }


    public double getLat() {
        return lat;
    }

    public double getLang() {
        return lang;
    }

    public String getPlaceurl() {
        return placeurl;
    }
}
