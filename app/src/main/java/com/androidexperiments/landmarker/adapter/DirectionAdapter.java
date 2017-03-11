package com.androidexperiments.landmarker.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidexperiments.landmarker.model.direction.Step;
import com.google.creativelabs.androidexperiments.typecompass.R;

import java.util.ArrayList;

/**
 * Created by indianic on 11/03/17.
 */

public class DirectionAdapter extends RecyclerView.Adapter<DirectionAdapter.ViewHolder> {
    private ArrayList<Step> stepsList;
    private Context context;


    public DirectionAdapter(Context context, ArrayList<Step> stepsList) {
        this.stepsList = stepsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_placediretion, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final Step step = stepsList.get(i);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            viewHolder.tvInstruction.setText(Html.fromHtml(step.getHtmlInstructions(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            viewHolder.tvInstruction.setText(Html.fromHtml(step.getHtmlInstructions()));
        }
        viewHolder.tvMode.setText(step.getTravelMode());
        viewHolder.tvDistance.setText(step.getDistance().getText().toString() + "," + step.getDuration().getText().toString());
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvInstruction;
        private TextView tvMode;
        private TextView tvDistance;

        public ViewHolder(View view) {
            super(view);
            tvInstruction = (TextView) view.findViewById(R.id.row_item_placediretion_tv_direction);
            tvMode = (TextView) view.findViewById(R.id.row_item_placediretion_tv_mode);
            tvDistance = (TextView) view.findViewById(R.id.row_item_placediretion_tv_distance);
        }
    }
}
