package com.oleaf.eighthours;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Circle extends View{
    public TypedArray colors;
    public static final float alpha_threshold = 30;
    Paint paint;
    Resources resources;
    RectF rectangle;
    Home home;
    Gestures gestures;
    Vibrator vibrator;
    boolean vibrated;
    float alpha, start_alpha;
    boolean dragging, onRight;
    Animation animation;

    public Circle(Context context){
        super(context);
        init(context);
    }
    public Circle(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(context);
    }
    public Circle(Context context, AttributeSet attributeSet, int defStyleAttr){
        super(context, attributeSet, defStyleAttr);
        init(context);
    }
    private void init(Context c){
        resources = c.getResources();
        home = (Home)getContext();
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(resources.getDimension(R.dimen.circle_stroke));
        float width = resources.getDimension(R.dimen.circle_width)-paint.getStrokeWidth();
        float height = resources.getDimension(R.dimen.circle_height)-paint.getStrokeWidth();
        rectangle = new RectF((resources.getDimension(R.dimen.circle_view_width)-width) / 2f, (resources.getDimension(R.dimen.circle_view_height)-height) / 2f, width, height);
        colors = resources.obtainTypedArray(R.array.colors);
        animation = AnimationUtils.loadAnimation(c, R.anim.circle_up);
        gestures = new Gestures(64, 400) {
            @Override
            protected void onTap(float x, float y) {
                Log.d("Single Tap", "" + calculateAlpha(x, y));
            }
            @Override
            protected void onDragStop(float x, float y) {
                if (!home.isMenuUp())
                    menuUp();
                calculateAlpha(x, y);
                invalidate();
            }
            @Override
            protected void onDrag(float x, float y) {
                calculateAlpha(x, y);
                dragging = true;
                home.updateText(convertAlpha(alpha-(start_alpha+90)));
                invalidate();
            }
            @Override
            protected void longPress(float x, float y) {
                if (!vibrated){
                    vibrated = true;
                    vibrator.vibrate(20);
                }
                Log.d("Long Press", ""+calculateAlpha(x,y));
            }
            @Override
            protected void longPressStop(float x, float y) {
                vibrated = false;
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBaseCircle(canvas);
        drawSpans(canvas);
        if (dragging){
            drawDragging(canvas);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Home.viewContains(this, event.getX(), event.getY(), 0.2f ))
            gestures.touchEvent(event);
        return true;
    }
    private float calculateAlpha(float x, float y){
        alpha = -1f * (float)(Math.toDegrees(Math.atan2(x-rectangle.width()/2.0f, y-rectangle.height()/2.0f))-180f);
        clampAlpha();
        return alpha;
    }
    private int convertAlpha(float ... a)   {
        if (a.length > 0){
            return Math.round(Math.round(a[0]/360f * home.maximum) / Home.grid) * Home.grid;
        }else{
            return Math.round(Math.round(alpha/360f * home.maximum) / Home.grid) * Home.grid;
        }
    }
    private void drawBaseCircle(Canvas canvas){
        paint.setStrokeWidth(resources.getDimension(R.dimen.circle_stroke));
        paint.setColor(resources.getColor(R.color.circle_color));
        canvas.drawArc(rectangle, 0, 360, true, paint);
    }
    private void drawSpans(Canvas canvas){
        paint.setStrokeWidth(resources.getDimension(R.dimen.arc_stroke));
        start_alpha = -90;
        for (Home.Span span : home.getSpans()){
            paint.setColor(colors.getColor(span.color_index, 0));
            drawArc(canvas, start_alpha, span.minutes/home.maximum * 360.0f);
            start_alpha += span.minutes/home.maximum * 360.0f;
        }
    }
    private void drawDragging(Canvas canvas){
        if (home.getChosen() < 0)
            paint.setColor(resources.getColor(R.color.drag_color));
        else
            paint.setColor(colors.getColor(home.getChosen(), 0 ));
        drawArc(canvas, start_alpha, alpha-(start_alpha+90));
    }
    private void drawArc(Canvas canvas, float startAngle, float sweepAngle){
        Path arc = new Path();
        arc.addArc(rectangle, startAngle, sweepAngle);
        canvas.drawPath(arc, paint);
    }
    private float clampAlpha(){
        if (alpha >= (360 - alpha_threshold * 2) && alpha < (360-alpha_threshold)){
            onRight = false;
        }else if (alpha >= alpha_threshold  && alpha <= alpha_threshold * 2){
            onRight = true;
        }
        if (start_alpha + 90 > alpha_threshold){
            onRight = false;
        }
        if (alpha >= (360 - alpha_threshold) && onRight){
            alpha = 0;
        }else if(alpha < alpha_threshold && !onRight){
            alpha = 360;
        }
        if (alpha < start_alpha + 90){
            alpha = start_alpha + 90;
        }
        return alpha;
    }

    /*
    menu functions
    TODO: Animation
     */
    public void menuUp(){
        home.popup();
    }
    public void cancel(){
        dragging = false;
        alpha = 0;
        invalidate();
        home.updateText(home.time_left);
    }
    public void confirm(){
        home.newActivity(convertAlpha(alpha-(start_alpha+90)), 1);
        home.updateText(home.time_left);
        dragging = false;
        alpha = 0;
        invalidate();
    }
}