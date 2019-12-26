package com.oleaf.eighthours;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceScreen;

import java.io.Serializable;

import static java.util.Arrays.copyOf;

public class Activities implements Parcelable, Serializable {
    private static final long serialversionUID = 22;
    public static final int grid = 20;
    public int maximum = 60 * 8;
    public int default_maximum = 60*8;
    public boolean maximum_changed = false;
    int time_left = maximum;
    private Span[] spans;


    Activities(int maximum){
        this.maximum = maximum;
        spans = new Span[0];
        time_left = maximum;
        updateDefault(maximum);
    }


    private Activities(Parcel in){
        readParcel(in);
    }
    public Span[] getSpans(){
        return spans;
    }
    public Span getSpan(int index) { return (spans.length > index) ? spans[index] : null;}
    public int getLength() { return spans.length; }
    public boolean isActivity() { return (spans.length > 0); }
    public int newActivity(int minutes, int color_index){
        return newActivity(minutes, color_index, "");
    }

    public int newActivity(int minutes, int color_index, String name){
        if (minutes < grid/2f){
            return -1;
        }
        Span[] cp = new Span[spans.length+1];
        spans = copyOf(spans, spans.length+1);
        if (name.isEmpty())
            spans[spans.length-1] = new Span(minutes, color_index, time_left);
        else
            spans[spans.length-1] = new Span(minutes, color_index, time_left, name);
        time_left -= spans[spans.length-1].minutes;
        return spans.length-1;
    }
    public int findColor(int search){
        for (int ix = spans.length-1; ix >= 0; --ix){
            if (spans[ix].color_index == search){
                return ix;
            }
        }
        return -1;
    }
    public void deleteActivity(int index){
        if (index >= spans.length || index < 0)
            return;
        Span[] cp = new Span[spans.length-1];
        for (int ix=0; ix < spans.length; ++ix){
            if (ix < index)
                cp[ix] = spans[ix];
            else if (ix > index)
                cp[ix-1] = spans[ix];
        }
        time_left += spans[index].minutes;
        spans = cp;
    }

    public void updateTimeLeft(){
        time_left = maximum;
        for (Span s : spans){
            time_left -= s.minutes;
        }
    }

    public void changeMaximum(int maximum){
        if (maximum == default_maximum){
            resetMaximum();
            return;
        }
        maximum_changed = true;
        this.maximum = maximum;
        updateTimeLeft();
    }

    public void resetMaximum(){
        maximum_changed = false;
        maximum = default_maximum;
        updateTimeLeft();
    }

    public void updateDefault(int default_maximum){
        this.default_maximum = default_maximum;
        if (!maximum_changed)
            maximum = default_maximum;
        updateTimeLeft();
    }

    public void startActivity(int index){
        if (index < 0 || index > spans.length -1)
            return;
        spans[index].start();
    }

    public void pauseActivity(int index){
        if (index < 0 || index > spans.length -1)
            return;
        spans[index].pause();
    }

    public void activityDone(int index){
        if (index < 0 || index > spans.length -1)
            return;
        spans[index].startTime = (long) (System.currentTimeMillis() - spans[index].minutes * 60000L);
    }

    public void editActivity(int index, int minutes, byte color){
        editActivity(index, minutes, color, "");
    }

    public void editActivity(int index, int minutes, byte color, String name){
        if (index < 0 || index > spans.length -1)
            return;
        time_left += spans[index].minutes - minutes;
        spans[index].minutes = minutes;
        if (name.isEmpty())
            spans[index].name = Span.default_name;
        else
            spans[index].name = name;
        spans[index].color_index = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void readParcel(Parcel in){
        maximum = in.readInt();
        time_left = in.readInt();
        spans = in.createTypedArray(Span.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(maximum);
        dest.writeInt(time_left);
        //dest.writeParcelable(calendar, );
        dest.writeTypedArray(spans, 0);
    }

    public float[] getAlphas(){
        float[] alphas = new float[spans.length];
        for (int a = 0; a < alphas.length; ++a){
            alphas[a] = spans[a].minutes / maximum * 360f;
        }
        return alphas;
    }

    public static final Parcelable.Creator<Activities> CREATOR
            = new Parcelable.Creator<Activities>(){
        public Activities createFromParcel(Parcel in){
            return new Activities(in);
        }

        public Activities[] newArray(int size){
            return new Activities[size];
        }
    };
}
