package com.androidexperiments.landmarker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.androidexperiments.landmarker.adapter.PlaceCategoryAdapter;
import com.androidexperiments.landmarker.model.PlaceCategoryModel;
import com.androidexperiments.landmarker.seekbar.MarkerSeekBar;
import com.androidexperiments.landmarker.util.Const;
import com.google.creativelabs.androidexperiments.typecompass.R;

import java.util.ArrayList;

/**
 * Created by ANKIT on 2/28/2017.
 */

public class PlaceCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<PlaceCategoryModel> placeCategory = new ArrayList<>();
    private RecyclerView rvCategory;
    private PlaceCategoryAdapter placeCategoryAdapter;
    private Button btnNearBy;
    private MarkerSeekBar markerSeekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placecategory);
        btnNearBy = (Button) findViewById(R.id.activity_placecategory_btn_nearbyplace);
        rvCategory = (RecyclerView) findViewById(R.id.activity_placecategory_rvCategory);
        markerSeekBar = (MarkerSeekBar) findViewById(R.id.activity_placecategory_sbRadius);
        loadCategory();
        placeCategoryAdapter = new PlaceCategoryAdapter(PlaceCategoryActivity.this, placeCategory);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PlaceCategoryActivity.this);
        rvCategory.setLayoutManager(mLayoutManager);
        rvCategory.setAdapter(placeCategoryAdapter);
        btnNearBy.setOnClickListener(this);
        markerSeekBar.setProgress(10);
        markerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                SharedPreferences.Editor editor=  LandmarkerApplication.getmInstance().getSharedPreferences().edit();
                editor.putInt(Const.PREF_RADIUS,i);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void loadCategory() {
        placeCategory.add(new PlaceCategoryModel("airport", "https://maps.gstatic.com/mapfiles/place_api/icons/airport-71.png"));
        placeCategory.add(new PlaceCategoryModel("atm", "https://maps.gstatic.com/mapfiles/place_api/icons/atm-71.png"));
        placeCategory.add(new PlaceCategoryModel("bank", "https://maps.gstatic.com/mapfiles/place_api/icons/bank_dollar-71.png"));
        placeCategory.add(new PlaceCategoryModel("bar", "https://maps.gstatic.com/mapfiles/place_api/icons/bar-71.png"));
        placeCategory.add(new PlaceCategoryModel("beauty_salon", "https://maps.gstatic.com/mapfiles/place_api/icons/barber-71.png"));
        placeCategory.add(new PlaceCategoryModel("bicycle_store", "https://maps.gstatic.com/mapfiles/place_api/icons/bicycle-71.png"));
        placeCategory.add(new PlaceCategoryModel("bus_station", "https://maps.gstatic.com/mapfiles/place_api/icons/bus-71.png"));
        placeCategory.add(new PlaceCategoryModel("bar", "https://maps.gstatic.com/mapfiles/place_api/icons/bar-71.png"));
        placeCategory.add(new PlaceCategoryModel("beautycafe_salon", "https://maps.gstatic.com/mapfiles/place_api/icons/cafe-71.png"));
        placeCategory.add(new PlaceCategoryModel("car_dealer", "https://maps.gstatic.com/mapfiles/place_api/icons/car_dealer-71.png"));
        placeCategory.add(new PlaceCategoryModel("car_rental", "https://maps.gstatic.com/mapfiles/place_api/icons/car_rental-71.png"));
        placeCategory.add(new PlaceCategoryModel("car_repair", "https://maps.gstatic.com/mapfiles/place_api/icons/car_repair-71.png"));
        placeCategory.add(new PlaceCategoryModel("casino", "https://maps.gstatic.com/mapfiles/place_api/icons/casino-71.png"));

        placeCategory.add(new PlaceCategoryModel("dentist", "https://maps.gstatic.com/mapfiles/place_api/icons/dentist-71.png"));
        placeCategory.add(new PlaceCategoryModel("doctor", "https://maps.gstatic.com/mapfiles/place_api/icons/doctor-71.png"));
        placeCategory.add(new PlaceCategoryModel("gas_station", "https://maps.gstatic.com/mapfiles/place_api/icons/gas_station-71.png"));
        placeCategory.add(new PlaceCategoryModel("hindu_temple", "https://maps.gstatic.com/mapfiles/place_api/icons/worship_hindu-71.png"));
        placeCategory.add(new PlaceCategoryModel("gym", "https://maps.gstatic.com/mapfiles/place_api/icons/fitness-71.png"));
        placeCategory.add(new PlaceCategoryModel("hospital", "https://maps.gstatic.com/mapfiles/place_api/icons/doctor-71.png"));
        placeCategory.add(new PlaceCategoryModel("jewelry_store", "https://maps.gstatic.com/mapfiles/place_api/icons/jewelry-71.png"));
        placeCategory.add(new PlaceCategoryModel("library", "https://maps.gstatic.com/mapfiles/place_api/icons/library-71.png"));
        placeCategory.add(new PlaceCategoryModel("mosque", "https://maps.gstatic.com/mapfiles/place_api/icons/worship_islam-71.png"));
        placeCategory.add(new PlaceCategoryModel("museum", "https://maps.gstatic.com/mapfiles/place_api/icons/museum-71.png"));
        placeCategory.add(new PlaceCategoryModel("movie_theater", "https://maps.gstatic.com/mapfiles/place_api/icons/movies-71.png"));
        placeCategory.add(new PlaceCategoryModel("pharmacy", "https://maps.gstatic.com/mapfiles/place_api/icons/doctor-71.png"));
        placeCategory.add(new PlaceCategoryModel("police", "https://maps.gstatic.com/mapfiles/place_api/icons/police-71.png"));
        placeCategory.add(new PlaceCategoryModel("post_office", "https://maps.gstatic.com/mapfiles/place_api/icons/post_office-71.png"));
        placeCategory.add(new PlaceCategoryModel("restaurant", "https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png"));
        placeCategory.add(new PlaceCategoryModel("school", "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png"));
        placeCategory.add(new PlaceCategoryModel("shopping_mall", "https://maps.gstatic.com/mapfiles/place_api/icons/shopping-71.png"));
        placeCategory.add(new PlaceCategoryModel("taxi_stand", "https://maps.gstatic.com/mapfiles/place_api/icons/taxi-71.png"));
        placeCategory.add(new PlaceCategoryModel("travel_agency", "https://maps.gstatic.com/mapfiles/place_api/icons/travel_agent-71.png"));
        placeCategory.add(new PlaceCategoryModel("university", "https://maps.gstatic.com/mapfiles/place_api/icons/university-71.png"));
        placeCategory.add(new PlaceCategoryModel("zoo", "https://maps.gstatic.com/mapfiles/place_api/icons/zoo-71.png"));


    }

    @Override
    public void onClick(View v) {
        if (v == btnNearBy) {
            String placeCategory = "";
            ArrayList<PlaceCategoryModel> mplaceCategory = new ArrayList<>();
            mplaceCategory = placeCategoryAdapter.getCategoryList();
            for (int i = 0; i < mplaceCategory.size(); i++) {
                if (mplaceCategory.get(i).isSelect()) {
                    placeCategory = placeCategory + mplaceCategory.get(i).getPlaceCategory() + "|";
                }
            }
            placeCategory = placeCategory.substring(0, placeCategory.length() - 1);
            Intent intent = new Intent(PlaceCategoryActivity.this, NearbyLocationActivity.class);
            intent.putExtra("CATEGORY", placeCategory);
            startActivity(intent);

        }
    }
}
