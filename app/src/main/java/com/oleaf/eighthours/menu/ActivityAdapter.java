package com.oleaf.eighthours.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.oleaf.eighthours.Activities;
import com.oleaf.eighthours.Home;
import com.oleaf.eighthours.R;
import com.oleaf.eighthours.Span;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {
    Activities activities;
    TypedArray colors;

    //it needs to be updated every second
    //direct reference to spans
    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name, time, number;
        public ProgressBar bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initialize all the necessary views
            name = (TextView) itemView.findViewById(R.id.activity_name);
            time = (TextView) itemView.findViewById(R.id.ac_time);
            number = (TextView) itemView.findViewById(R.id.number);
            bar = (ProgressBar) itemView.findViewById(R.id.progress);
            //add buttons:
        }
    }
    ActivityAdapter(Activities a, TypedArray c){
        activities = a;
        colors = c;
    }

    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.menu_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ActivityAdapter.ViewHolder viewHolder, int position) {
        Span s = activities.getSpan(position);
        int minutes = (int)s.getMinutes();
        int seconds = (int) Math.floor(minutes * 60) % 60;
        int color = colors.getColor(s.getColorIndex(), 0xFF000000);

        viewHolder.name.setText(s.getName());
        viewHolder.time.setText(((int) Math.floor(minutes / 60f)) + ":" + ((minutes < 10) ? "0"+minutes : ""+minutes) + ":" + ((seconds < 10) ? "0"+seconds : ""+seconds));
        viewHolder.number.setText("#"+(position + 1));
        viewHolder.bar.updateProgress(s.getPart());
        viewHolder.bar.changeColor(color);
        viewHolder.number.setTextColor(color);
    }

    @Override
    public int getItemCount(){
        return activities.getSpans().length;
    }
}
