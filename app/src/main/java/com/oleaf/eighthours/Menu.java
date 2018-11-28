package com.oleaf.eighthours;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Menu extends View {
    public static final float pause = 0.4f;
    private static final float bigger = 0.4f;
    private static final float tollerance = 0.2f;
    public static final int animation_duration = 150;
    private float rounded_radius;
    private int chosen=-1, next_chosen=-1;
    boolean up;
    private Resources resources;
    private RectF[] rectangles;
    private TypedArray colors;
    private Gestures gestures;
    private Paint paint;
    private ValueAnimator va, def;
    private Home home;

    Menu(Context context){
        super(context);
        init(context);
    }
    Menu(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(context);
    }
    Menu(Context context, AttributeSet attributeSet, int a){
        super(context, attributeSet, a);
        init(context);
    }
    private void init(Context context){
        resources = context.getResources();
        home = (Home) getContext();
        colors = resources.obtainTypedArray(R.array.colors);
        rectangles = new RectF[colors.length()];
        final float rect_width = resources.getDimension(R.dimen.circle_width) / (rectangles.length *(1 + pause) + pause);
        for(int index=0; index < rectangles.length; ++index)
            rectangles[index] = new RectF(index * (1 + pause) * rect_width + pause * rect_width, 0, index * (1 + pause) * rect_width + pause * rect_width + rect_width, resources.getDimension(R.dimen.menu_button_height));
        rounded_radius = resources.getDimension(R.dimen.menu_button_height)/2.0f;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        gestures = new Gestures(64, 400) {
            @Override
            protected void onTap(float x, float y) {
                if (!va.isRunning()){
                    next_chosen = indexPressed(x, y);
                    if (next_chosen != chosen){
                        def.start();
                        home.chosenChanged();
                    }
                }
            }
            @Override
            protected void onDragStop(float x, float y) {}
            @Override
            protected void onDrag(float x, float y) {}
            @Override
            protected void longPress(float x, float y) {}
            @Override
            protected void longPressStop(float x, float y) {}
        };
        ValueAnimator a = new ValueAnimator();
        va = new ValueAnimator(); def = new ValueAnimator();
        va.setFloatValues(0f, 1f); def.setFloatValues(1f, 0f);
        va.setDuration(animation_duration); def.setDuration(animation_duration/3);
        ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x=pause*rect_width;
                for (int index = 0; index < rectangles.length; ++index){
                    //TODO: performance
                    if(index == chosen)
                        rectangles[index].right = x + rect_width * (1 + (float)animation.getAnimatedValue() * bigger);
                    else
                        rectangles[index].right = x + rect_width * (1 - (float)animation.getAnimatedValue() * bigger / (colors.length()-1));
                    rectangles[index].left = x;
                    x = rectangles[index].right + rect_width * pause;
                }
                invalidate();
            }
        };
        va.addUpdateListener(animatorUpdateListener); def.addUpdateListener(animatorUpdateListener);
        //maybe get rid of it
        def.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (chosen == -1)
                    def.end();
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                chosen = next_chosen;
                va.start();
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }
    @Override
    protected void onDraw(Canvas canvas) {
        for (int ix = 0; ix < rectangles.length; ++ix){
            drawRect(ix, canvas);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestures.touchEvent(event);
        return true;
    }
    private void drawRect(int index, Canvas canvas){
        paint.setColor(colors.getColor(index, 0));
        canvas.drawRect(rectangles[index], paint);
        //drawRounded(rectangles[index], canvas);
    }
    private void drawRounded(RectF rect, Canvas canvas){
        canvas.drawCircle(rect.left+rounded_radius, rounded_radius, rounded_radius, paint);
        canvas.drawCircle(rect.right-rounded_radius, rounded_radius, rounded_radius, paint);
        canvas.drawRect(rect.left+rounded_radius, rect.top, rect.right-rounded_radius, rect.bottom, paint);
    }
    private int indexPressed(float x, float y){
        if (!Home.viewContains(this, x, y))
            return -1;
        for (int index = 0; index < rectangles.length; ++index){
            if (rectContains(rectangles[index], x))
                return index;
        }
        return -1;
    }
    public int getChosen(){
        return next_chosen;
    }
    public void popUp(){
        resetRect();
        chosen = -1;
        //setVisibility(View.VISIBLE);
        invalidate();
    }
    private static boolean rectContains(RectF rect, float x){
        return x <= rect.right + rect.width() * tollerance;
    }
    private void resetRect(){
        final float rect_width = resources.getDimension(R.dimen.circle_width) / (rectangles.length *(1 + pause) + pause);
        for(int index=0; index < rectangles.length; ++index)
            rectangles[index] = new RectF(index * (1 + pause) * rect_width + pause * rect_width, 0, index * (1 + pause) * rect_width + pause * rect_width + rect_width, resources.getDimension(R.dimen.circle_stroke));
    }
}