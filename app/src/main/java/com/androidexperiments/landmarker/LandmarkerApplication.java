package com.androidexperiments.landmarker;

import android.app.Application;
import android.util.Log;

import com.google.creativelabs.androidexperiments.typecompass.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * setup fonts and shit
 */
public class LandmarkerApplication extends Application
{
    private static final String TAG = LandmarkerApplication.class.getSimpleName();
    private int deviceHeight;
    private int deviceWidth;
    private static LandmarkerApplication mInstance = null;
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
