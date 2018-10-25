package com.oleaf.eighthours;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class Circle extends View {
    public TypedArray colors;
    Paint paint;
    Resources resources;
    RectF rectangle;
    Home home;
    float alpha;
    boolean dragging = false;

    public boolean is_gitWorking(){
        return true;
    }

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
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(resources.getDimension(R.dimen.circle_stroke));
        float width = resources.getDimension(R.dimen.circle_width)-paint.getStrokeWidth();
        float height = resources.getDimension(R.dimen.circle_height)-paint.getStrokeWidth();
        rectangle = new RectF((resources.getDimension(R.dimen.circle_view_width)-width) / 2f, (resources.getDimension(R.dimen.circle_view_height)-height) / 2f, width, height);
        colors = resources.obtainTypedArray(R.array.colors);
        home = (Home)getContext();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //TODO
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
        Log.d("Circle", ""+event.getActionMasked());
        if (event.getActionMasked() == MotionEvent.ACTION_MOVE){
            Log.d("Circle", ""+calculateAlpha(event.getX(), event.getY()));
            dragging = true;
            invalidate();
        }else if(event.getActionMasked() == MotionEvent.ACTION_UP){
            dragging = false;
            invalidate();
        }
        return true;
    }
    private float calculateAlpha(float x, float y){
        alpha = -1f * (float)(Math.toDegrees(Math.atan2(x-rectangle.width()/2.0f, y-rectangle.height()/2.0f))-180f);
        return alpha;
    }
    private void drawBaseCircle(Canvas canvas){
        paint.setStrokeWidth(resources.getDimension(R.dimen.circle_stroke));
        paint.setColor(resources.getColor(R.color.circle_color));
        canvas.drawArc(rectangle, 0, 360, true, paint);
    }
    private void drawSpans(Canvas canvas){
        paint.setStrokeWidth(resources.getDimension(R.dimen.arc_stroke));
        float s_alpha = -90;
        for (Home.Span span : home.spans){
            paint.setColor(colors.getColor(span.color_index, 0));
            drawArc(canvas, s_alpha, span.minutes/home.maximum * 360.0f);
            s_alpha += span.minutes/home.maximum * 360.0f;
        }
    }
    private void drawDragging(Canvas canvas){
        paint.setColor(resources.getColor(R.color.drag_color));
        drawArc(canvas, -90, alpha);
    }
    private void drawArc(Canvas canvas, float startAngle, float sweepAngle){
        Path arc = new Path();
        arc.addArc(rectangle, startAngle, sweepAngle);
        canvas.drawPath(arc, paint);
    }
}