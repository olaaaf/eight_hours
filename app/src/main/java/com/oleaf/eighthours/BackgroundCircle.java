package com.oleaf.eighthours;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class BackgroundCircle extends View {
    private float xCenter;
    private RectF rectangle;
    private Paint paint;
    private float radius;
    private float stroke;
    private int backgroundColor;

    public BackgroundCircle(Context context) {
        super(context);
        init(context);
    }
    public BackgroundCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public BackgroundCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public BackgroundCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    void init(Context  c){
            Resources r = c.getResources();
            final float w = r.getDimension(R.dimen.circle_width);
        final float h = r.getDimension(R.dimen.circle_height);
        stroke = r.getDimension(R.dimen.circle_rounded);
        backgroundColor = r.getColor(R.color.colorPrimary);
        post(new Runnable() {
            @Override
            public void run() {
                float x = (getWidth() - w)/2f;
                float y = (getHeight() - h)/2f;
                rectangle = new RectF(x, y, w+x, h+y);
                radius = w / 2f;
                xCenter = getWidth()/2f;
                invalidate();
            }
        });
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw the lines
        drawLines(canvas, 45, 14);
        //draw the outer circle
        drawOuter(canvas);
        drawInner(canvas);
    }

    public void drawOuter(Canvas canvas){
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(backgroundColor);

        Path rightBottom = new Path();
        Path leftBottom = new Path();
        Path leftTop = new Path();
        Path rightTop = new Path();
        rightBottom.addArc(rectangle,0, 90);
        leftBottom.addArc(rectangle, 90, 90);
        leftTop.addArc(rectangle, 180, 90);
        rightTop.addArc(rectangle, 270, 90);
        rightBottom.lineTo(rectangle.centerX(), getHeight());
        rightBottom.lineTo(getWidth(), getHeight());
        rightBottom.lineTo(getWidth(), rectangle.centerY());
        leftBottom.lineTo(0, rectangle.centerY());
        leftBottom.lineTo(0, getHeight());
        leftBottom.lineTo(rectangle.centerX(), getHeight());
        leftTop.lineTo(rectangle.centerX(), 0);
        leftTop.lineTo(0, 0);
        leftTop.lineTo(0, rectangle.centerY());
        rightTop.lineTo(getWidth(), rectangle.centerY());
        rightTop.lineTo(getWidth(), 0);
        rightTop.lineTo(rectangle.centerX(), 0);
        rightBottom.close();
        leftBottom.close();
        leftTop.close();
        rightTop.close();
        canvas.drawPath(rightBottom, paint);
        canvas.drawPath(leftBottom, paint);
        canvas.drawPath(leftTop, paint);
        canvas.drawPath(rightTop, paint);
    }

    public void drawLines(Canvas canvas, float angle, int lines){
        float lineWidth = (2f * radius) / (2f * lines + 1);       //each drawn line is followed by an invisible one (a pause)
        paint.setStrokeWidth(lineWidth/2f);
        paint.setStyle(Paint.Style.STROKE);
        angle = (float) Math.atan(Math.tan(Math.toRadians(angle)));
        float a = (float) Math.tan(angle);
        float dX = (float) Math.cos(angle) * lineWidth;
        float startX = xCenter - (3 * radius) +dX;
        final float staticY1 = startX * a;
        final float staticY2 = (startX + 4 * radius )* a;
        paint.setColor(Color.WHITE & 0x99FFFFFF);
        for (; startX < 2 * radius; startX += dX*2){
            canvas.drawLine(startX, staticY1, startX + 2*radius, staticY2, paint);
        }
    }

    public void drawInner(Canvas canvas){
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(rectangle.centerX(), rectangle.centerY(), radius - stroke, paint);
    }

}
