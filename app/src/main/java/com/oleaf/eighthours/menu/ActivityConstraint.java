package com.oleaf.eighthours.menu;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.oleaf.eighthours.Span;

import java.util.concurrent.atomic.AtomicBoolean;

public class ActivityConstraint extends ConstraintLayout {
    boolean playing = false;
    Span span;
    ProgressBar bar;
    TextView time;
    int position;
    ActivityUpdater updater;
    final OnClickListener play = new OnClickListener() {
        @Override
        public void onClick(View v) {
            pressPlay(v);
        }
    };
    final OnClickListener minus = new OnClickListener() {
        @Override
        public void onClick(View v) {
            pressMinus(v);
        }
    };
    final OnClickListener plus = new OnClickListener() {
        @Override
        public void onClick(View v) {
            pressPlus(v);
        }
    };

    public ActivityConstraint(Context context) {
        super(context);
        init(context);
    }

    public ActivityConstraint(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ActivityConstraint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context c){

    }

    public void update(){
        //Actual updating goes here
        bar.updateProgress(span.getPart());
        updateText(span.getMinutes() - span.getCurrentMinutes());
    }

    public void pressPlay(View view){
         if (!playing) {
            update();
            startTimer();
            span.start();
        }
        else {
            stopTimer();
            span.pause();
        }
        playing = !playing;
    }

    public void pressMinus(View view){

    }

    public void pressPlus(View view){

    }

    private void startTimer(){
        updater.start(position);
    }

    private void stopTimer(){
        updater.stop(position);
    }

    private void updateText(float min){
        int h = (int) Math.floor(min/60f);
        int sec = (int) ((min % 1.0f) * 60);
        time.setText((h/10.0f < 1.0f ? "0" : "") + h + ":" + ((min%60) / 10f < 1f ? "0" :"") + ((int) min % 60) +":" + (sec / 10.0f < 1.0f ? "0" : "") + sec);
    }


}
