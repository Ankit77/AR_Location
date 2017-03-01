package com.androidexperiments.landmarker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidexperiments.landmarker.model.PlaceCategoryModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.creativelabs.androidexperiments.typecompass.R;

import java.util.ArrayList;

/**
 * Created by ANKIT on 3/1/2017.
 */

public class PlaceCategoryAdapter extends RecyclerView.Adapter<PlaceCategoryAdapter.ViewHolder> {
    private ArrayList<PlaceCategoryModel> categoryList;
    private Context context;

    public ArrayList<PlaceCategoryModel> getCategoryList() {
        return categoryList;
    }

    public PlaceCategoryAdapter(Context context, ArrayList<PlaceCategoryModel> categoryList) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public PlaceCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_activity_place_category, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceCategoryAdapter.ViewHolder viewHolder, final int i) {
        final PlaceCategoryModel placeCategoryModel = categoryList.get(i);
        viewHolder.tvCategory.setText(categoryList.get(i).getPlaceCategory());
        viewHolder.chkCategory.setOnCheckedChangeListener(null);
        viewHolder.chkCategory.setChecked(placeCategoryModel.isSelect());

        Glide.with(context).load(categoryList.get(i).getPlaceUrl())
                .thumbnail(0.5f).placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imgCategory);
        viewHolder.chkCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                placeCategoryModel.setSelect(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategory;
        private ImageView imgCategory;
        private CheckBox chkCategory;

        public ViewHolder(View view) {
            super(view);

            tvCategory = (TextView) view.findViewById(R.id.row_activity_placecategory_tv_category);
            imgCategory = (ImageView) view.findViewById(R.id.row_activity_placecategory_img_category);
            chkCategory = (CheckBox) view.findViewById(R.id.row_activity_placecategory_chk_category);
        }
    }
}
