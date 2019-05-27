package com.oleaf.eighthours;

import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PlayMenu extends LinearLayout {
    private AnimatedVectorDrawableCompat start2pause, pause2start;
    private boolean handlerRunning, notificationShown;
    private static final long updateF = 100;
    private Notification notification;
    private Animation showA, hideA;
    private Activities activities;
    private Runnable runnable;
    private float elevation;
    private Handler handler;
    private boolean state;      //true: onGoing, false: stopped

    public PlayMenu(Context context) {
        super(context);
        init(context);
    }

    public PlayMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PlayMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context c){
        Resources r = c.getResources();
        showA = AnimationUtils.loadAnimation(c, R.anim.menu_popup);
        hideA = AnimationUtils.loadAnimation(c, R.anim.menu_down);

        start2pause = AnimatedVectorDrawableCompat.create(c, R.drawable.playtostop);
        pause2start = AnimatedVectorDrawableCompat.create(c, R.drawable.stoptoplay);
        elevation = r.getDimension(R.dimen.play_elevation);
        activities = ((Home) getContext()).activities;

        //any view that is invisible or has alpha of 0 doesn't initialize at start
        //the following code helps to both: initialize and hide it with a short alpha animation
        setVisibility(VISIBLE);
        AlphaAnimation quickHide = new AlphaAnimation(1f, 0f);
        quickHide.setFillAfter(true);
        quickHide.setDuration(1);
        startAnimation(quickHide);

        //used to update
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                update();
                handler.postDelayed(runnable, updateF);
            }
        };
    }

    public void show(){
        setElevation(elevation);
        startAnimation(showA);
    }

    public void hide(){
        setElevation(0);
        startAnimation(hideA);
    }

    private void update(){
        Span x = activities.getSpan(((Home)getContext()).circle.arcs.draggingIndex);
        ((Home) getContext()).updateBottom(x.getMinutes(), x.minutes);
    }

    public void startPress(View view){
        //TODO: press ghosting?, delay
        if (handlerRunning)
            handler.removeCallbacks(runnable);
        state = !state;
        changeButton((ImageView) view, state);
        if (state){
            handler.postDelayed(runnable, updateF);
            handlerRunning = true;
            ((Home) getContext()).circle.startSelected();
        }else{
            handlerRunning = false;
            ((Home) getContext()).circle.stopSelected();
        }
    }

    /**
     * Play an animation or change the image
     * @param view the button to change the drawable
     * @param toWhat false: pause to start, true: start to pause
     * @return whether animated or static change
     */
    public boolean changeButton(ImageView view, boolean toWhat){
        view.setImageDrawable((toWhat) ? start2pause : pause2start);
        ((AnimatedVectorDrawableCompat) view.getDrawable()).start();
        return true;
    }
}
