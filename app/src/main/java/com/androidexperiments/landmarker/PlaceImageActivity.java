package com.androidexperiments.landmarker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidexperiments.landmarker.adapter.CustomPagerAdapter;
import com.androidexperiments.landmarker.model.placedetail.Photo;
import com.androidexperiments.landmarker.util.Const;
import com.google.creativelabs.androidexperiments.typecompass.R;

import java.util.ArrayList;

/**
 * Created by indianic on 20/03/17.
 */

public class PlaceImageActivity extends AppCompatActivity {
    private CustomPagerAdapter customPagerAdapter;
    private ArrayList<Photo> placePhotos = new ArrayList<>();
    private ViewPager mViewPager;
    private TextView tvTitle;
    private ImageView imgBack;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeimage);
        if (getIntent().getExtras() != null) {
            title = getIntent().getExtras().getString(Const.KEY_TITLE, "");
        }
        tvTitle = (TextView) findViewById(R.id.activity_placeimage_tv_title);
        imgBack = (ImageView) findViewById(R.id.activity_placeimage_img_back);
        placePhotos.addAll(LandmarkerApplication.getmInstance().getPlacesphotos());
        customPagerAdapter = new CustomPagerAdapter(PlaceImageActivity.this, placePhotos);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(customPagerAdapter);
        tvTitle.setText(title);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
