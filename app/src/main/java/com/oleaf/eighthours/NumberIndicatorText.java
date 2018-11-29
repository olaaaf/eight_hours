package com.oleaf.eighthours;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class NumberIndicatorText extends AppCompatTextView {
    private static final int duration = 300;
    private static final int total_rotation = 205;
    ValueAnimator valueAnimator;
    PointF center;
    Paint paint;
    int count;
    int color;

    NumberIndicatorText(Context context){
        super(context);
        init(context);
    }
    NumberIndicatorText(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(context);
    }
    NumberIndicatorText(Context context, AttributeSet attributeSet, int a){
        super(context, attributeSet, a);
        init(context);
    }

    void init(Context context){
        paint = new Paint();
        color = context.getResources().getColor(R.color.indicator_circle);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        post(new Runnable() {
            @Override
            public void run() {
                center = new PointF(getWidth()/2.0f, getHeight()/2.0f);
            }
        });
        valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(0, 1.4f, 1f);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setScaleX((float) animation.getAnimatedValue());
                setScaleY((float) animation.getAnimatedValue());
                setRotation((float) animation.getAnimatedValue() * total_rotation / 360.0f);
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(center.x, center.y, center.x, paint);
        super.onDraw(canvas);
    }

    public void update(int x){
        char characters[] = String.valueOf(x).toCharArray();
        setText(characters, 0, characters.length);
        if (valueAnimator.isRunning())
            valueAnimator.end();
        if (x == 1){
            setVisibility(VISIBLE);
            //pop up animation
            valueAnimator.setFloatValues(0, 1.4f, 1f);
            valueAnimator.start();
        }else if(x == 0){
            valueAnimator.setFloatValues(1f, 1.4f, 0f);
            valueAnimator.start();
        }else{
            valueAnimator.setFloatValues(1f, 1.4f, 0.6f, 1.4f, 1f);
            valueAnimator.start();
        }
    }
}
