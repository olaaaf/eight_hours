package com.oleaf.eighthours;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class CMenu extends ConstraintLayout {
    //Animation drawables
    //Describes timer state with: 0 - timer off, 1 - timer running, 2 - timer paused
    private Home home;
    public CMenu(Context context) {
        super(context);
        init(context);
    }
    public CMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public CMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    protected void init(Context context){
        Resources resources = context.getResources();
        home = (Home) getContext();
    }

    public void show(int index){
        home.rotateCMenu((int) (home.circle.getAngleBefore(index)
                + home.circle.convertMinutes((int)home.activities.getSpan(index).minutes) / 2.0f));
        setVisibility(VISIBLE);
        //TODO: animation
    }


    public void hide(){
        home.rotateCMenu(0);
        setVisibility(INVISIBLE);
    }

}
