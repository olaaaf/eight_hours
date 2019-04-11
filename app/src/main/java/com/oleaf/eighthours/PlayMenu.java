package com.oleaf.eighthours;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class PlayMenu extends LinearLayout {
    Animation showA, hideA;
    float elevation;

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
        elevation = r.getDimension(R.dimen.play_elevation);
    }

    public void show(){

    }

    public void hide(){

    }

    public void startPress(){

    }
}
