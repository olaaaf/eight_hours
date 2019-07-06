package com.oleaf.eighthours.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.oleaf.eighthours.Activities;
import com.oleaf.eighthours.Home;
import com.oleaf.eighthours.R;
import com.oleaf.eighthours.Span;

import java.util.Random;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {
    Activities activities;
    TypedArray colors;
    int expanded=-1;
    int previous=-1;
    RecyclerView r;

    //it needs to be updated every second
    //direct reference to spans
    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name, time, number;
        public View arrow;
        public ProgressBar bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initialize all the necessary views
            name = itemView.findViewById(R.id.activity_name);
            time = itemView.findViewById(R.id.ac_time);
            number = itemView.findViewById(R.id.number);
            bar = itemView.findViewById(R.id.progress);
            arrow = itemView.findViewById(R.id.arrowE);
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
        r = (RecyclerView) parent;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ActivityAdapter.ViewHolder viewHolder, final int position) {
        Span s = activities.getSpan(position);
        int minutes = (int) s.getCurrentMinutes();
        int seconds = (int) Math.floor(minutes * 60) % 60;
        int color = colors.getColor(s.getColorIndex(), 0xFF000000);

        viewHolder.name.setText(s.getName());
        viewHolder.time.setText(((int) Math.floor(minutes / 60f)) + ":" + ((minutes < 10) ? "0"+minutes : ""+minutes) + ":" + ((seconds < 10) ? "0"+seconds : ""+seconds));
        viewHolder.number.setText("#"+(position + 1));
        viewHolder.bar.updateProgress(s.getPart());
        viewHolder.bar.changeColor(color);
        viewHolder.number.setTextColor(color);
        viewHolder.name.setTextColor(color);

        //Add onClickListener - when clicked, expand the view
        //expanded is a global variable holding the expanded position
        final boolean isExpanded = (expanded == position);
        if (isExpanded)
            previous = position;

        //Expand the view - set visibility

        viewHolder.itemView.findViewById(R.id.activityButton).setVisibility(!isExpanded ? View.GONE:View.VISIBLE);
        viewHolder.itemView.findViewById(R.id.minus30).setVisibility(!isExpanded ? View.GONE:View.VISIBLE);
        viewHolder.itemView.findViewById(R.id.plus30).setVisibility(!isExpanded ? View.GONE: View.VISIBLE);
        viewHolder.itemView.setActivated(isExpanded);

        //animate the arrow - @animator/arrow
        viewHolder.arrow.setActivated(isExpanded);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded = isExpanded ? -1 : position;
                notifyItemChanged(previous);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount(){
        return activities.getSpans().length;
    }
}
