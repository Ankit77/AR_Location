package com.androidexperiments.landmarker.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidexperiments.landmarker.model.placedetail.Photo;
import com.androidexperiments.landmarker.util.Const;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.creativelabs.androidexperiments.typecompass.R;

import java.util.ArrayList;

/**
 * Created by indianic on 20/03/17.
 */

public class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private ArrayList<Photo> imageList = new ArrayList<>();

    public CustomPagerAdapter(Context context, ArrayList<Photo> images) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageList.addAll(images);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        String photourl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + imageList.get(position).getWidth() + "&photoreference=" + imageList.get(position).getPhotoReference() + "&key=" + Const.PLACES_API_KEY;
        Glide.with(mContext).load(photourl)
                .thumbnail(0.5f).placeholder(R.drawable.ic_placeholder)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
