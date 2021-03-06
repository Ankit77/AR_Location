package com.androidexperiments.landmarker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidexperiments.landmarker.model.placedetail.Photo;
import com.androidexperiments.landmarker.util.Const;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.creativelabs.androidexperiments.typecompass.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by indianic on 17/03/17.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    private ArrayList<Photo> photoList = new ArrayList<>();
    private Context context;
    private ImageClickListner imageClickListner;

    public void setImageClickListner(ImageClickListner imageClickListner) {
        this.imageClickListner = imageClickListner;
    }

    public ImagesAdapter(Context context, List<Photo> photoList) {
        this.photoList.addAll(photoList);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_images_placeinfo, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        String photourl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + photoList.get(i).getWidth() + "&photoreference=" + photoList.get(i).getPhotoReference() + "&key=" + Const.PLACES_API_KEY;
        if (i == 3) {
            viewHolder.imgImage.setImageResource(R.drawable.more_images);
        } else {
            Glide.with(context).load(photourl)
                    .thumbnail(0.5f).placeholder(R.drawable.ic_placeholder)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.imgImage);
        }
        viewHolder.imgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageClickListner != null) {
                    imageClickListner.onImageClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (photoList.size() > 4) {
            return 4;
        } else {
            return photoList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgImage;

        public ViewHolder(View view) {
            super(view);
            imgImage = (ImageView) view.findViewById(R.id.row_images_placinfo_imgImages);
        }
    }

    public interface ImageClickListner {
        public void onImageClick(int position);
    }
}
