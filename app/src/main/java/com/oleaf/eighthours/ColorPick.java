package com.oleaf.eighthours;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;

public class ColorPick extends View {
    Paint paint;
    ValueAnimator animation;
    private int before;
    public float dim, y_addition;
    public float[] dimension;
    public static final int duration = 100;
    public static final float perc = 0.8f;
    public static final float smaller = 0.5f;
    public boolean i = false;
    public ColorPick(Context context) {
        super(context);
    }

    public ColorPick(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPick(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ColorPick(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init() {
        dimension = new float[((ColorMenu)getParent()).colors.length()];
        dim = perc * Tools.clamp(getWidth() / (float) (((ColorMenu)getParent()).colors.length() * 2 - 1),0f, (float) getHeight());
        Arrays.fill(dimension, dim);
        y_addition = (getHeight() - dim) / 2.0f;

        paint = new Paint();
        paint.setAntiAlias(true);
        animation = new ValueAnimator();
        animation.setFloatValues(perc, 1);
        animation.setDuration(duration);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (int ix = 0; ix < dimension.length; ++ix){
                    if (ix == ((ColorMenu)getParent()).chosen){
                        dimension[ix] = (dim / perc) * (float) animation.getAnimatedValue();
                    }else if (ix == before){
                        dimension[ix] = (dim / perc) * (perc + (1 - (float) animation.getAnimatedValue()));
                    }
                }
                invalidate();
            }
        });
    }

    private void drawAll(Canvas canvas){
        if (!i){
            i = true;
            init();
        }

        for (int ix = 0; ix < ((ColorMenu)getParent()).colors.length(); ++ix){
            paint.setColor(((ColorMenu)getParent()).colors.getColor(ix, 0));
            canvas.drawCircle(ix * 2 * dim/perc + dim / (2.0f * perc), (dim/( perc * 2.0f)) + y_addition, dimension[ix] / 2.0f, paint);
            paint.setColor(android.graphics.Color.WHITE);
            if (ix == ((ColorMenu)getParent()).chosen)
                canvas.drawCircle(ix * 2 * dim/perc + dim / (2.0f * perc), (dim/(perc*2.0f)) + y_addition, dimension[ix] / 2.0f * smaller , paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
            before = ((ColorMenu)getParent()).chosen;
            //TODO: Fix that
            ((ColorMenu)getParent()).chosen = Tools.clamp((int) ((event.getX() / getWidth()) * (dimension.length)), 0, dimension.length);
            if (before != ((ColorMenu)getParent()).chosen) {
                animation.end();
                animation.start();
            }
        }
        return true;
    }

    public void noAnimation(){
        resetDimensions();
        dimension[((ColorMenu)getParent()).chosen] = dim / perc;
        invalidate();
    }

    private void resetDimensions(){
        Arrays.fill(dimension, dim);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawAll(canvas);
    }
}