package com.androidexperiments.landmarker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.androidexperiments.landmarker.data.NearbyPlace;
import com.androidexperiments.landmarker.sensors.HeadTracker;
import com.androidexperiments.landmarker.util.HeadTransform;
import com.androidexperiments.landmarker.widget.DirectionalTextView;
import com.androidexperiments.landmarker.widget.DirectionalTextViewContainer;
import com.androidexperiments.landmarker.widget.IntroView;
import com.androidexperiments.landmarker.widget.SwingPhoneView;
import com.google.creativelabs.androidexperiments.typecompass.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import se.walkercrou.places.Place;


public class MainActivity extends BaseActivity implements
        SurfaceHolder.Callback, DirectionalTextView.onPlaceClick {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private boolean mResolvingError = false;

    @InjectView(R.id.intro_view)
    IntroView mIntroView;
    @InjectView(R.id.swing_phone_view)
    SwingPhoneView mSwingPhoneView;
    @InjectView(R.id.directional_text_view_container)
    DirectionalTextViewContainer mDirectionalTextViewContainer;
    @InjectView(R.id.maps_button_view_container)
    View mMapsButtonViewContainer;

    private NearbyPlace mCurrentPlace;
    private HeadTracker mHeadTracker;
    private HeadTransform mHeadTransform;
    private Handler mTrackingHandler = new Handler();
    private boolean mIsTracking = false;
    private float[] mEulerAngles = new float[3];

    private boolean mHasPlaces = false;
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResolvingError = savedInstanceState != null && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);
        mHasPlaces = false;

        initViews();
        initSensors();

        loadSurfaceview();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        LandmarkerApplication.getmInstance().setDeviceHeight(height);
        LandmarkerApplication.getmInstance().setDeviceWidth(width);
        //location is fine, update places
        getNewPlaces();
    }

    private void loadSurfaceview() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    private void initViews() {
        ButterKnife.inject(this);
        mDirectionalTextViewContainer.getmEast().setOnPlaceClick(this);
        mDirectionalTextViewContainer.getmNorth().setOnPlaceClick(this);
        mDirectionalTextViewContainer.getmWest().setOnPlaceClick(this);
        mDirectionalTextViewContainer.getmSouth().setOnPlaceClick(this);
        mSwingPhoneView.setVisibility(View.GONE);
        mDirectionalTextViewContainer.setVisibility(View.GONE);
    }

    private void initSensors() {
        mHeadTracker = HeadTracker.createFromContext(this);
        mHeadTransform = new HeadTransform();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //events
        EventBus.getDefault().register(this);

        //sensors
        mHeadTracker.startTracking();

        //drawing
        mDirectionalTextViewContainer.startDrawing();

        //resuming from pause/maps
        if (mHasPlaces)
            startTracking();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }

    @Override
    protected void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);

        mIsTracking = false;
        mHeadTracker.stopTracking();

        mDirectionalTextViewContainer.stopDrawing();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //butterknife

    @OnClick(R.id.maps_button_view)
    public void onMapsButtonClick() {
        if (mCurrentPlace == null) {
            Log.w(TAG, "No currentPlace available - must be empty. Ignore click.");
            return;
        }

        try {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=" + URLEncoder.encode(mCurrentPlace.getName(), "UTF-8"))
            );
            //cheating!
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.maps_button_close)
    public void onMapsViewCloseClicked() {
        hideMapsButtonView();
    }

    @OnClick(R.id.maps_button_view_container)
    public void onContainerClick() {
        //do nothing - just need registered for onClick so it doesnt get passed through
    }

    //overrides

    @Override
    public void onBackPressed() {
        if (mMapsButtonViewContainer.getVisibility() == View.VISIBLE)
            hideMapsButtonView();
        else
            super.onBackPressed();
    }


    //event bus


    private void showMapsButtonView() {
        mMapsButtonViewContainer.setVisibility(View.VISIBLE);
        Animation anim = new AlphaAnimation(0.f, 1.f);
        anim.setDuration(300);
        mMapsButtonViewContainer.startAnimation(anim);
    }

    private void hideMapsButtonView() {
        mMapsButtonViewContainer.setVisibility(View.GONE);
        Animation anim = new AlphaAnimation(1.f, 0.f);
        anim.setDuration(300);
        mMapsButtonViewContainer.startAnimation(anim);
    }


    private void getNewPlaces() {
        //update introview
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIntroView.setIsFindingPlaces();
            }
        });
        new AsyncTask<Void, Void, List<Place>>() {
            private ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setTitle(getString(R.string.app_name));
                progressDialog.setMessage("Loading nearby place");
                progressDialog.show();
            }

            @Override
            protected List<Place> doInBackground(Void... params) {
                return LandmarkerApplication.getmInstance().getPlaces();
            }

            @Override
            protected void onPostExecute(List<Place> places) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (places == null) {
                    Toast.makeText(
                            MainActivity.this,
                            "There are no places near you - Please try again later.",
                            Toast.LENGTH_LONG
                    ).show();

                    goBackToSplash();
                    return;
                }
                mHasPlaces = true;
                startTracking();
                if (LandmarkerApplication.getmInstance().getCurrentLocation() != null) {
                    mDirectionalTextViewContainer.updatePlaces(places, LandmarkerApplication.getmInstance().getCurrentLocation());
                } else {
                    Location location = new Location("Ahmedabad");
                    location.setLatitude(23.036406);
                    location.setLongitude(72.517845);
                    mDirectionalTextViewContainer.updatePlaces(places, location);
                }

                showSwingPhoneView();
            }
        }.execute();
    }

    private void showSwingPhoneView() {
        mIntroView.animateOut();

        //animate in triggers its own animate out once completed, the next method
        mSwingPhoneView.animateIn();
    }

    public void onEvent(SwingPhoneView.OnAnimateOutCompleteEvent event) {
        mDirectionalTextViewContainer.animateIn();
    }

    private void goBackToSplash() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //unneccessary with finish()?
        startActivity(intent);

        this.finish();
    }

    private void startTracking() {
        mIsTracking = true;

        mTrackingHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!mIsTracking) return;

                mHeadTracker.getLastHeadView(mHeadTransform.getHeadView(), 0);
                mHeadTransform.getEulerAngles(mEulerAngles, 0);

                runOnUiThread(updateDirectionalTextView);

                mTrackingHandler.postDelayed(this, 100);
            }
        });
    }

    private Runnable updateDirectionalTextView = new Runnable() {
        @Override
        public void run() {
            mDirectionalTextViewContainer.updateView(Math.toDegrees(mEulerAngles[1]));
        }
    };


    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // open the camera
            camera = Camera.open();
            camera.setDisplayOrientation(90);
        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();

        // modify parameter
        camera.setParameters(param);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public void onClick(NearbyPlace nearbyPlace) {
        mCurrentPlace = nearbyPlace;
        showMapsButtonView();
        Toast.makeText(MainActivity.this, nearbyPlace.getName(), Toast.LENGTH_LONG).show();
    }
}
