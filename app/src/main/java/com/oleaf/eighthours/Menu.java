package com.oleaf.eighthours;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Menu extends View {
    public static final float pause = 0.4f;
    Resources resources;
    TypedArray colors;
    RectF[] rectangles;
    float rounded_radius;
    Paint paint;
    boolean up;

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

    void init(Context context){
        resources = context.getResources();
        colors = resources.obtainTypedArray(R.array.colors);
        rectangles = new RectF[colors.length()];
        float rect_width = resources.getDimension(R.dimen.circle_width) / (rectangles.length *(1 + pause) + pause);
        for(int index=0; index < rectangles.length; ++index)
            rectangles[index] = new RectF(index * (1 + pause) * rect_width + pause * rect_width, 0, index * (1 + pause) * rect_width + pause * rect_width + rect_width, resources.getDimension(R.dimen.circle_stroke));
        rounded_radius = resources.getDimension(R.dimen.circle_stroke)/2.0f;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    //popup tools
    public void popUp(){
        setVisibility(View.VISIBLE);
        invalidate();
        up = true;
    }
    public void down(){
        up = false;
    }
    public boolean isUp(){
        return up;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int ix = 0; ix < rectangles.length; ++ix){
            drawRect(ix, canvas);
        }
    }

    void drawRect(int index, Canvas canvas){
        paint.setColor(colors.getColor(index, 0));
        drawRounded(rectangles[index], canvas);
    }

    void drawRounded(RectF rect, Canvas canvas){
        canvas.drawCircle(rect.left+rounded_radius, rounded_radius, rounded_radius, paint);
        canvas.drawCircle(rect.right-rounded_radius, rounded_radius, rounded_radius, paint);
        canvas.drawRect(rect.left+rounded_radius, rect.top, rect.right-rounded_radius, rect.bottom, paint);
    }
}

//TODO: clicking