package com.androidexperiments.landmarker;

import android.app.Application;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;

import com.androidexperiments.landmarker.model.direction.DirectionModel;
import com.google.creativelabs.androidexperiments.typecompass.R;

import java.util.ArrayList;
import java.util.List;

import se.walkercrou.places.Place;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * setup fonts and shit
 */
public class LandmarkerApplication extends Application
{
    private Location currentLocation;
    private static final String TAG = LandmarkerApplication.class.getSimpleName();
    private int deviceHeight;
    private int deviceWidth;
    private static LandmarkerApplication mInstance = null;
    private ArrayList<Point> placeXY=new ArrayList<>();
    private List<Place> places=new ArrayList<>();
    private DirectionModel directionModel;

    public DirectionModel getDirectionModel() {
        return directionModel;
    }

    public void setDirectionModel(DirectionModel directionModel) {
        this.directionModel = directionModel;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "oh nooo");

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/texgyreheros-bold.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        mInstance=this;

    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public ArrayList<Point> getPlaceXY() {
        return placeXY;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public void setPlaceXY(ArrayList<Point> placeXY) {
        this.placeXY = placeXY;
    }

    public static LandmarkerApplication getmInstance() {
        return mInstance;
    }

    public int getDeviceWidth() {
        return deviceWidth;
    }

    public void setDeviceWidth(int deviceWidth) {
        this.deviceWidth = deviceWidth;
    }

    public int getDeviceHeight() {
        return deviceHeight;
    }

    public void setDeviceHeight(int deviceHeight) {
        this.deviceHeight = deviceHeight;
    }
}
