package com.oleaf.eighthours.details;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.oleaf.eighthours.R;

public class ProgressCircle extends View {
    Paint paint;
    RectF rectangle;
    int greyColor;
    static final float alpha_pause = 10;
    float alpha_rounded;
    float progress;
    private boolean afterPost=false;

    public ProgressCircle(Context context) {
        super(context);
        init(context);
    }

    public ProgressCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ProgressCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context c){
        Resources r = c.getResources();

        final float sW = r.getDimension(R.dimen.circle_rounded);
        paint = new Paint();
        paint.setStrokeWidth(sW);
        post(new Runnable() {
            @Override
            public void run() {
                afterPost = true;
                rectangle = new RectF(sW, sW, getWidth()- sW, getHeight() - sW);
                alpha_rounded = (float) Math.asin(paint.getStrokeWidth() / (getHeight() / 2f));
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw progress with pause
        if (afterPost)
            drawRounded(-90, progress*360f, canvas);
    }

    private float drawRounded(float startAlpha, float a, Canvas canvas){
        float startAngle = alpha_pause + startAlpha;
        float sweepAngle = a - alpha_pause * 2;
        sweepAngle = (sweepAngle < 0) ? 0 : sweepAngle;

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(rectangle.width()/2f * (float) Math.cos(Math.toRadians(startAngle+alpha_rounded)) + rectangle.left + rectangle.width()/2.0f, rectangle.height()/2f + rectangle.top + rectangle.width()/2f * (float) Math.sin(Math.toRadians(startAngle+alpha_rounded)), paint.getStrokeWidth() / 2f, paint);
        canvas.drawCircle(rectangle.width()/2f * (float) Math.cos(Math.toRadians(sweepAngle-alpha_rounded+startAngle)) + rectangle.left + rectangle.width()/2.0f, rectangle.height()/2.0f + rectangle.top + rectangle.width()/2f * (float) Math.sin(Math.toRadians(sweepAngle-alpha_rounded+startAngle)), paint.getStrokeWidth() / 2f, paint);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rectangle, startAngle+alpha_rounded, sweepAngle-alpha_rounded, false, paint);
        return a;
    }

    public void updateProgress(float p){
        progress = p;
    }

    public void setColor(int c){
        paint.setColor(c);
    }
}
