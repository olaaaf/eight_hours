package com.oleaf.eighthours.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;

import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.oleaf.eighthours.R;

public class SimpleLine extends View {
    //Attributes
    private int number_of_lines=1;
    private float break_to_line=0.2f;
    private boolean rounded=true;

    //UI variables (depending on the size)
    private float lineWidth=0;
    private float breakWidth=0;
    private float height=0;

    public SimpleLine(Context context) {
        super(context);
        init(context, null);
    }

    public SimpleLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SimpleLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public SimpleLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    void init(Context c, AttributeSet attributeSet){
        if (attributeSet != null){
            TypedArray attrs = c.getTheme().obtainStyledAttributes(attributeSet, R.styleable.SimpleLine, 0, 0);
            try{
                number_of_lines = attrs.getInt(R.styleable.SimpleLine_number_of_lines, 1);
                break_to_line = attrs.getFloat(R.styleable.SimpleLine_break_to_line_ratio, 0.2f);
                rounded = attrs.getBoolean(R.styleable.SimpleLine_rounded, false);
            }finally {
                attrs.recycle();
            }
        }
        //After the whole thing initialized
        post(new Runnable() {
            @Override
            public void run() {
                height = getHeight();
                //get width
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = 0;
        for (int ix = 0; ix < number_of_lines; ++ix){
            if (rounded)
                drawRounded(canvas, x, lineWidth);
            else
                drawLine(canvas, x, lineWidth);
            x += lineWidth + breakWidth;
        }
    }

    private void drawLine(Canvas canvas, float startX, float width){

    }

    private void drawRounded(Canvas canvas, float startX, float width){
        //draw two half-circles radius = height/2
        //left

        //right

        //draw line
        drawLine(canvas, startX + height/2f, width - height/2f);
    }

}
