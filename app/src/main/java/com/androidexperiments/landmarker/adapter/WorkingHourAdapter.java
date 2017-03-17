package com.androidexperiments.landmarker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.creativelabs.androidexperiments.typecompass.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by indianic on 17/03/17.
 */

public class WorkingHourAdapter extends RecyclerView.Adapter<WorkingHourAdapter.ViewHolder> {
    private ArrayList<String> workinghourList;
    private Context context;


    public WorkingHourAdapter(Context context, List<String> workinghourList) {
        this.workinghourList.addAll(workinghourList);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_workinghour_placeinfo, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.tvWorkingHours.setText(workinghourList.get(i));
    }

    @Override
    public int getItemCount() {
        return workinghourList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvWorkingHours;

        public ViewHolder(View view) {
            super(view);
            tvWorkingHours = (TextView) view.findViewById(R.id.row_workinghour_placeinfo_tv_wh);
        }
    }
}
