package com.oleaf.eighthours;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class ProgressBar extends View {
    private float radius, width;
    private int progressColor;
    private Paint paint = new Paint();
    private float minPart;

    public ProgressBar(Context context) {
        super(context);
        init(context);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context c){
        radius = getHeight() / 2f;
        width = getWidth();
        progressColor = ContextCompat.getColor(c, R.color.progress_color);
        minPart = getHeight() / getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas){
        //Draw the background bar for reference
        //paint.setColor(Color.WHITE);
        //drawBar(canvas, 1f);

        //Draw the actual progress
        paint.setColor(progressColor);
        drawBar(canvas, 1f);
    }

    private void drawBar(Canvas canvas, float part){
        if (part < minPart){
            canvas.drawCircle(radius, radius, radius, paint);
        }
        else{
            canvas.drawCircle(radius, radius, radius, paint);
            canvas.drawRect(2* radius, 0, part * (width - (2 * radius)), 2 * radius, paint);
            canvas.drawCircle(radius + part * (width - (2 * radius)), radius, radius, paint);
        }
    }

    private void bubbleAnimation(Canvas canvas){

    }
}
