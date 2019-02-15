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
    public int timerState;
    public int[] order;
    private Home home;
    public ImageView[] buttons;
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

    //Public functions
    public boolean isVisible(){
        return (getVisibility() == VISIBLE);
    }


    public void show(int index){
        if (index < home.activities.getLength())
            show (home.activities.getSpan(index));
    }

    public void show(Span span){

    }

    public void hide(){
        //TODO: hide animations of all buttons
    }

}
