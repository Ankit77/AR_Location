package com.androidexperiments.landmarker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidexperiments.landmarker.model.placedetail.Review;
import com.androidexperiments.landmarker.util.Utils;
import com.google.creativelabs.androidexperiments.typecompass.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by indianic on 17/03/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private ArrayList<Review> reviewList = new ArrayList<>();
    private Context context;


    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.reviewList.addAll(reviewList);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_reviews_placeinfo, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Review review = reviewList.get(i);
        if (TextUtils.isEmpty(review.getRelativeTimeDescription())) {
            viewHolder.tvdate.setText(Utils.convertMilistoTime(Long.parseLong(review.getTime().toString())));
        } else {
            viewHolder.tvdate.setText(review.getRelativeTimeDescription());
        }
        viewHolder.tvUsername.setText(review.getAuthorName());
        viewHolder.tvReview.setText(review.getText());
        if (review.getRating() != null) {
            viewHolder.rbRate.setRating(review.getRating());
        } else {
            viewHolder.rbRate.setRating(0.0f);
        }

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername;
        private TextView tvdate;
        private TextView tvReview;
        private RatingBar rbRate;

        public ViewHolder(View view) {
            super(view);
            tvUsername = (TextView) view.findViewById(R.id.row_reviews_placeinfo_tv_user);
            tvdate = (TextView) view.findViewById(R.id.row_reviews_placeinfo_tv_datetime);
            tvReview = (TextView) view.findViewById(R.id.row_reviews_placeinfo_tv_review);
            rbRate = (RatingBar) view.findViewById(R.id.row_reviews_placeinfo_rb_rate);
        }
    }
}
