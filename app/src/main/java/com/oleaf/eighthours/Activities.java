package com.oleaf.eighthours;

import android.content.Context;

import static java.util.Arrays.copyOf;

public class Activities {
    public static final int grid = 20;
    public int maximum = 60 * 8;
    int time_left = maximum;
    public Calendar calendar;
    private Span[] spans;

    Activities(boolean a){
        spans = new Span[0];
    }

    Activities(Context context){
        spans = new Span[0];
        calendar = new Calendar(context);
    }

    public Span[] getSpans(){
        return spans;
    }
    public int getLength() { return spans.length; }
    public void newActivity(int minutes, int color_index){
        if (minutes < grid/2f){
            return;
        }
        Span[] cp = new Span[spans.length+1];
        spans = copyOf(spans, spans.length+1);
        spans[spans.length-1] = new Span(minutes, color_index);
    }
    public int findColor(int search){
        for (int ix = spans.length-1; ix >= 0; --ix){
            if (spans[ix].color_index == search){
                return ix;
            }
        }
        return -1;
    }
    public boolean deleteActivity(int index){
        if (index >= spans.length || index < 0)
            return false;
        Span[] cp = new Span[spans.length-1];
        for (int ix=0; ix < spans.length; ++ix){
            if (ix < index)
                cp[ix] = spans[ix];
            else if (ix > index)
                cp[ix-1] = spans[ix];
        }
        return true;
    }

    class Span{
        float minutes;
        byte color_index;
        Span(int _minutes, int _color_index){
            minutes = clamp(round_up(_minutes));
            color_index = (byte) _color_index;
            time_left -= minutes;
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
