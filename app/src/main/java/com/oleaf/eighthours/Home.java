package com.oleaf.eighthours;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Home extends AppCompatActivity {
    public static final int grid = 20;
    public int maximum = 60 * 8;
    public int time_left = maximum;
    public Span[] spans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        spans = new Span[0];
    }
    public Span[] getSpans(){
        return spans;
    }
    public void newActivity(int minutes, int color_index){
        int ix = findColor(color_index)+1;
        Span[] cp = new Span[spans.length+1];
        for (int i = 0; i < ix; ++i){
            cp[i] = spans[i];
        }
        cp[ix] = new Span(minutes, color_index);
        for (int i = ix+1; i < spans.length+1; ++i){
            cp[i] = spans[i-1];
        }
        spans = cp.clone();
    }
    private int findColor(int search){
        for (int ix = spans.length-1; ix >= 0; --ix){
            if (spans[ix].color_index == search){
                return ix;
            }
        }
        return -1;
    }
    public class Span{
        float minutes;
        byte color_index;
        public Span(int _minutes, int _color_index){
            minutes = clamp(round_up(_minutes));
            color_index = (byte) _color_index;
        }
        private int round_up(int time) {
            return Math.round(time / grid) * grid;
        }
        //no built in clamp function
        private int clamp(int time){
            if (time > time_left){
                time = time_left;
            }else if(time < 0){
                time = 0;
            }
            return time;
        }
    }
}
