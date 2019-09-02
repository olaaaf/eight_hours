package com.oleaf.eighthours.details;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import com.oleaf.eighthours.R;

public class ProgressBar extends View {
    private float radius, width, minPart, progress;
    private int progressColor;
    private int primaryColor;
    private Paint paint = new Paint();

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
        paint.setAntiAlias(true);
        progressColor = ContextCompat.getColor(c, R.color.progress_color);
        post(new Runnable() {
            @Override
            public void run() {
                radius = getHeight() / 2f;
                width = getWidth();
                minPart = getHeight() / (float) getWidth();
                updateProgress(minPart);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas){
        //Draw the background bar for reference
        paint.setColor(progressColor);
        drawBar(canvas, 1f);

        //Draw the actual progress
        paint.setColor(primaryColor);
        drawBar(canvas, progress);
    }

    private void drawBar(Canvas canvas, float part){
        if (part == 0)
            return;
        if (part <= minPart){
            canvas.drawCircle(radius, radius, radius, paint);
        }
        else{
            canvas.drawCircle(radius, radius, radius, paint);
            canvas.drawRect(radius, 0, part * (width - 1 * radius), 2 * radius, paint);
            canvas.drawCircle( part * (width - 1 *radius), radius, radius, paint);
        }
    }

    private void bubbleAnimation(Canvas canvas){

    }

    public void setColor(int color){
        primaryColor = color;
    }

    public void updateProgress(float progress){
        this.progress = progress;
        invalidate();
    }
}
