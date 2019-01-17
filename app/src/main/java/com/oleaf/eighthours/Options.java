package com.oleaf.eighthours;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.util.AttributeSet;
import android.view.View;

public class Options extends android.support.v7.widget.AppCompatImageView {
    //Animation drawables
    private Drawable show, to_pause, to_start;
    //Describes timer state with: 0 - timer off, 1 - timer running, 2 - timer paused
    private int timerState;

    public Options(Context context) {
        super(context);
        init(context);
    }
    public Options(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public Options(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context){
        Resources resources = context.getResources();
        show = resources.getDrawable(R.drawable.play_show, null);
        to_pause = resources.getDrawable(R.drawable.play_to_pause, null);
        to_start = resources.getDrawable(R.drawable.to_play, null);
    }

    //Public functions
    public boolean isVisible(){
        return (getVisibility() == VISIBLE);
    }

    public void show(){
        setVisibility(VISIBLE);
        startAnimation(show);
    }

    public void hide(){
        //TODO: hide animations of all buttons
    }

    public void onClick(){
        if (timerState == 1){
            pauseTimer();
            startAnimation(to_start);
            timerState = 2;
        }else if (timerState == 2){
            resumeTimer();
            startAnimation(to_pause);
            timerState = 1;
        }else{
            startTimer();
            startAnimation(to_pause);
            timerState = 1;
        }

    }

    //Private functions
    private void startAnimation(){
        Drawable d = getDrawable();
        if (d instanceof AnimatedVectorDrawable)
            ((AnimatedVectorDrawable) d).start();
        else if(d instanceof AnimatedVectorDrawableCompat)
            ((AnimatedVectorDrawableCompat) d).start();
    }
    private void startAnimation(Drawable changeAnimation){
        setImageDrawable(changeAnimation);
        startAnimation();
    }
    private void pauseTimer(){

    }
    private void resumeTimer(){

    }
    private void startTimer(){

    }
}
