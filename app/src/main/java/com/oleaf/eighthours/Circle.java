package com.oleaf.eighthours;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

public class Circle extends View{
    public TypedArray colors;
    public static final float alpha_threshold = 45;
    private float alpha_pause;
    private float alpha_rounded;
    Paint paint;
    Resources resources;
    RectF rectangle;
    Home home;
    Gestures gestures;
    float alpha, start_alpha;
    boolean dragging, onRight, full;        //full - whether there's some time left
    Animation animation;
    List<ArcAnimation> arcAnimations;
    ArcAnimation draggingAnimation;
    private Handler handlerUpdater;
    private Runnable runnable;
    private boolean handlerWorking;
    private static final int millisUpdate = 3;
    //TODO: delete
    public static final boolean rounded = true;

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
        handlerUpdater = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
                handlerUpdater.postDelayed(runnable, millisUpdate);
            }
        };
        arcAnimations = (List<ArcAnimation>) new ArrayList();
        draggingAnimation = new ArcAnimation(-1, 0, convertMinutes(Activities.grid));
        draggingAnimation.stop();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        if (!rounded)
            paint.setStrokeWidth(resources.getDimension(R.dimen.circle_stroke));
        else
            paint.setStrokeWidth(resources.getDimension(R.dimen.circle_rounded));

        float width = resources.getDimension(R.dimen.circle_width)-paint.getStrokeWidth() / 2.0f;
        float height = resources.getDimension(R.dimen.circle_height)-paint.getStrokeWidth() / 2.0f;

        alpha_pause = resources.getDimension(R.dimen.alpha_break);
        rectangle = new RectF(paint.getStrokeWidth() / 2.0f, paint.getStrokeWidth() / 2.0f, width, height);
        colors = resources.obtainTypedArray(R.array.colors);
        animation = AnimationUtils.loadAnimation(c, R.anim.circle_up);
        gestures = new Gestures(64, -1) {
            @Override
            protected void onTap(float x, float y) {
                calculateRadius(x, y); //TODO: click threshold
                calculateAlpha(x, y);
            }
            @Override
            protected void onDragStop(float x, float y) {
                if (!full){
                    calculateAlpha(x, y);
                    if (!home.isMenuUp())
                        menuUp();
                    invalidate();
                }
            }
            @Override
            protected void onDrag(float x, float y) {
                if (!full && dragging && draggingAnimation.finished()){
                    calculateAlpha(x, y);
                    home.updateText(convertAlpha(alpha-(start_alpha+90)));
                    invalidate();
                }
            }
            @Override
            protected void longPress(float x, float y) {

            }
            @Override
            protected void longPressStop(float x, float y) {

            }
        };
        alpha_rounded = (float) Math.asin(paint.getStrokeWidth() / (height / 2f));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //drawBaseCircle(canvas);
        drawSpans(canvas);
        if (dragging){
            drawDragging(canvas);
        }
        checkAnimations();
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
    private float calculateRadius(float x, float y){
        return (float) (Math.sqrt(x*x + y*y));
    }

    private int convertAlpha(float ... a)   {
        if (a.length > 0){
            return Math.round(Math.round(a[0]/360f * home.activities.maximum) / Home.grid) * Home.grid;
        }else{
            return Math.round(Math.round(alpha/360f * home.activities.maximum) / Home.grid) * Home.grid;
        }
    }
    private float convertMinutes(int a){
        return ((float)a)/((float)home.activities.maximum)*360.0f;
    }
    private void drawBaseCircle(Canvas canvas){
        float stroke_width = paint.getStrokeWidth();
        paint.setStrokeWidth(resources.getDimension(R.dimen.circle_stroke));
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(resources.getColor(R.color.circle_color));
        canvas.drawArc(rectangle, 0, 360, true, paint);
        paint.setStrokeWidth(stroke_width);
    }
    private void drawSpans(Canvas canvas){
        start_alpha = -90;
        int add_alpha = 0;
        for (int index=0; index < home.activities.getLength(); ++index){
            paint.setColor(colors.getColor(home.activities.getSpans()[index].color_index, 0));
            if (index == home.activities.getLength() - 1)
                add_alpha = 0;
            drawArcRounded(canvas, start_alpha, add_alpha+convertMinutes((int) home.activities.getSpans()[index].minutes), true);
            start_alpha += home.activities.getSpans()[index].minutes/home.activities.maximum * 360.0f;
        }
    }
    private void drawDragging(Canvas canvas){
        paint.setColor(resources.getColor(R.color.drag_color));
        if (home.getChosen() > -1){
            paint.setColor(colors.getColor(home.getChosen(), 0));
        }
        if (!draggingAnimation.finished())
            drawArcRounded(canvas, start_alpha, draggingAnimation.getAlpha(), true);//(home.activities.isActivity()));
        else
            drawArcRounded(canvas, start_alpha, alpha - (start_alpha + 90), true);
    }
    private void drawArc(Canvas canvas, float startAngle, float sweepAngle){
        canvas.drawArc(rectangle, startAngle, sweepAngle, false, paint);
    }

    private void drawArcRounded(Canvas canvas, float startAngle, float sweepAngle, boolean addBreak){
        if (addBreak){
            startAngle += alpha_pause;
            sweepAngle -= alpha_pause * 2;
        }
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(rectangle.width()/2f * (float) Math.cos(Math.toRadians(startAngle+alpha_rounded)) + rectangle.left + rectangle.width()/2.0f, rectangle.height()/2f + rectangle.top + rectangle.width()/2f * (float) Math.sin(Math.toRadians(startAngle+alpha_rounded)), paint.getStrokeWidth() / 2f, paint);
        canvas.drawCircle(rectangle.width()/2f * (float) Math.cos(Math.toRadians(sweepAngle-alpha_rounded+startAngle)) + rectangle.left + rectangle.width()/2.0f, rectangle.height()/2.0f + rectangle.top + rectangle.width()/2f * (float) Math.sin(Math.toRadians(sweepAngle-alpha_rounded+startAngle)), paint.getStrokeWidth() / 2f, paint);
        paint.setStyle(Paint.Style.STROKE);
        drawArc(canvas, startAngle+alpha_rounded, sweepAngle-alpha_rounded);
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
        if (convertAlpha(alpha - (start_alpha + 90)) < Activities.grid) {
            alpha = convertMinutes(Activities.grid) + start_alpha + 90;
        }
        return alpha;
    }
    private void addAnimation(int id, float alpha){
        arcAnimations.add(new ArcAnimation(id, alpha));
    }
    private void addAnimation(int id, int millis, float alpha){
        arcAnimations.add(new ArcAnimation(id, millis, alpha));
    }
    private void arcAnimation(){
        if (!handlerWorking){
            handlerUpdater.postDelayed(runnable, millisUpdate);
            handlerWorking = true;
        }
    }
    private void checkAnimations(){
        if (!handlerWorking)
            return;
        handlerWorking = false;
        if (!draggingAnimation.finished()){
            handlerWorking = true;
            return;
        }
        for (int ix = 0; ix < arcAnimations.size(); ++ix){
            if (!arcAnimations.get(ix).finished()){
                handlerWorking = true;
                return;
            }
        }
        if (!handlerWorking)
            handlerUpdater.removeCallbacks(runnable);
    }
    /*
    menu functions
        TODO: arc show up animation
        TODO: deleting with the account of [full]
        TODO: animation when activity added
        TODO: maybe add arc shadow
        TODO: direction detection (on the circle)
     */
    public void menuUp(){
        if (convertAlpha() > 0)
            home.popup();
        else
            dragging = false;
    }
    public void cancel(){
        dragging = false;
        alpha = 0;
        invalidate();
        home.updateText(home.activities.time_left);
    }
    public void confirm() {
        home.addActivity(convertAlpha(alpha-(start_alpha+90)), home.getChosen());
        home.updateText(home.activities.time_left);
        if (home.activities.time_left <= 0)
            full = true;
        dragging = false;
        alpha = 0;
        invalidate();
    }

    public void addNew(){
        if (!dragging){
            dragging = true;
            alpha = convertMinutes((int) clamp(home.activities.time_left, Activities.grid,Activities.grid*3)) + start_alpha + 90;
            draggingAnimation.start(alpha);
            arcAnimation();
            invalidate();
            menuUp();
            home.updateText(convertAlpha(alpha-(start_alpha+90)));
        }
    }
    public static float clamp(float v, float min, float max){
        return ((v > max) ? max : ((v < min) ? min : v));
    }

    private class ArcAnimation{
        private static final int default_time = 50;
        int id;     // id of activity, if equalling -1, then pointing at dragging
        float time;
        long startTime;
        float addAlpha;
        float initialAlpha;

        ArcAnimation(int id, float alpha){
            this(id, default_time, alpha, -1);
        }
        ArcAnimation(int id, int milliseconds, float alpha, float additionalAlpha){
            this.id = id;
            time = (float) milliseconds;
            startTime = System.currentTimeMillis();
            initialAlpha = alpha;
            addAlpha = additionalAlpha;
        }
        ArcAnimation(int id, float alpha, float additionalAlpha){
            this(id, default_time, alpha, additionalAlpha);
        }
        float getAlpha(){
            if (addAlpha < 0)
                return (getPart() * initialAlpha);
            return (getPart() * (initialAlpha - addAlpha) + addAlpha);
        }

        float getPart(){
            if (launched())
                return clamp((System.currentTimeMillis() - startTime) / time, 0f, 1f);
            else
                return 0;
        }
        boolean finished(){
            return (System.currentTimeMillis() - startTime >= time) & (launched());
        }

        //optional [without start]
        void start(float alpha){
            initialAlpha = alpha;
            start();
        }
        void start(){ startTime = System.currentTimeMillis(); }
        void stop(){ startTime = -1; }
        boolean launched(){ return (startTime > -1);}
    }
}