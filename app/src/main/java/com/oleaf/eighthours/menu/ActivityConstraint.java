package com.oleaf.eighthours.menu;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.oleaf.eighthours.R;
import com.oleaf.eighthours.Span;

public class ActivityConstraint extends ConstraintLayout {
    boolean playing = false;
    Span span;
    ProgressBar bar;
    TextView time;
    final int playd = R.drawable.play_na, stopd = R.drawable.stop_na;
    int position=-1;
    ActivityUpdater updater=null;

    final OnClickListener play = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(pressPlay(v))
                setButtonStop(v);
            else
                setButtonStart(v);
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
        post(new Runnable() {
            @Override
            public void run() {
                bar.updateProgress(span.getPart());
            }
        });
    }

    public void update(){
        //Actual updating goes here
        bar.updateProgress(span.getPart());
        updateText(span.getMinutes() - span.getCurrentMinutes());
        if (playing && span.shouldStop())
            stopTimer();
    }

    public boolean pressPlay(View view){
        playing = !playing;
        if (playing) {
            update();
            startTimer();
            span.start();
        }
        else {
            stopTimer();
            span.pause();
        }
        return playing;
    }

    public void pressMinus(View view){
        span.addActiveMinutes(-0.5f);
        update();
    }

    public void pressPlus(View view){
        span.addActiveMinutes(0.5f);
        update();
    }

    public void startTimer(){
        updater.start(position);
    }

    public void stopTimer(){
        updater.stop(position);
    }

    public void setButtonStart(View v){
        ((ImageView) v).setImageResource(playd);

    }

    public void setButtonStop(View v){
        ((ImageView) v).setImageResource(stopd);
    }

    private void updateText(float min){
        int h = (int) Math.floor(min/60f);
        int sec = (int) ((min % 1.0f) * 60);
        time.setText((h/10.0f < 1.0f ? "0" : "") + h + ":" + ((min%60) / 10f < 1f ? "0" :"") + ((int) min % 60) +":" + (sec / 10.0f < 1.0f ? "0" : "") + sec);
    }

}
