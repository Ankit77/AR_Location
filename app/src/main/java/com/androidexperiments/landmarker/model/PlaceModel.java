package com.androidexperiments.landmarker.model;

import android.graphics.Point;

import se.walkercrou.places.Place;

/**
 * Created by ANKIT on 3/21/2017.
 */

public class PlaceModel {
    private Point point;
    private Place place;

    public PlaceModel() {
    }

    public PlaceModel(Point point, Place place) {
        this.point = point;
        this.place = place;
    }

    public Point getPoint() {
        return point;
    }

    public Place getPlace() {
        return place;
    }
}
