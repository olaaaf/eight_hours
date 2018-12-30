package com.oleaf.eighthours;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

public class CircleMenu extends ConstraintLayout {
    private static final float x_tint=0.4f, y_tint=1f;    //relative to width and height
    private int indexToAnimate = 0;
    private PropertyValuesHolder valuesHolderX, valuesHolderY;
    private static final int duration = 83;
    ObjectAnimator animation;

    public CircleMenu(Context context) {
        super(context);
        Initialise(context);
    }
    public CircleMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        Initialise(context);
    }
    public CircleMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Initialise(context);
    }

    private void Initialise(Context context){
        animation = new ObjectAnimator();
        animation.setDuration(duration);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void showUp(float x, float y, float alpha){
        setVisibility(VISIBLE);
        setX(x - x_tint * getWidth());
        setY(y - y_tint * getHeight());
        setRotation((alpha - 180));
        animation();
    }

    private void animation(){
        if (indexToAnimate >= getChildCount())
            return;
        valuesHolderX = PropertyValuesHolder.ofFloat(TRANSLATION_X, 0, (float) (Math.cos((Math.PI / (2 * (getChildCount() - 1))) * indexToAnimate - Math.PI/2.0f) * getWidth()));
        valuesHolderY = PropertyValuesHolder.ofFloat(TRANSLATION_Y,
                0, getHeight() * (1 - (float) (Math.sin((Math.PI / (2 * (getChildCount() - 1))) * indexToAnimate + Math.PI/2.f))));
        View a = getChildAt(indexToAnimate);
        animation = ObjectAnimator.ofPropertyValuesHolder(a, valuesHolderX, valuesHolderY);
        animation.start();

        indexToAnimate++;
    }

    public void changeTint(){

    }

    public void detectButtons(float x, float y){
        if (getVisibility() == INVISIBLE)
            return;

        int index = -1;
        for (int ix = 0; ix < getChildCount(); ++ix){
            View view = getChildAt(ix);
            int location[] = new int[2];
            view.getLocationOnScreen(location);
            if (containsOnScreen(location, view.getWidth(), view.getHeight(), x, y)){
                index = ix;
                break;
            }
        }
        Log.d("Circle", ""+index);
        switch (index){
            case 0:
                //Log.d("Circle", "0");
                break;
            case 1:

                break;
            case 2:
                break;
        }
    }

    public static boolean containsOnScreen(int[] loc, int width, int height, float pointerX, float pointerY){
        return !(pointerX < loc[0] || pointerX > loc[0] + width || pointerY < loc[1] || pointerY > loc[1] + height);
    }

}
