package com.oleaf.eighthours;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import static java.util.Arrays.copyOf;

public class Activities implements Parcelable {
    public static final int grid = 20;
    public int maximum = 60 * 8;
    int time_left = maximum;
    private Span[] spans;


    Activities(boolean a){
        spans = new Span[0];
    }

    Activities(Context context){
        //Recovery and file management
        this(false);
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
        if (minutes < grid/2f){
            return -1;
        }
        Span[] cp = new Span[spans.length+1];
        spans = copyOf(spans, spans.length+1);
        spans[spans.length-1] = new Span(minutes, color_index, time_left);
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
        time_left += spans[index].minutes;
        spans = cp;
        return true;
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
        if (index < 0 || index > spans.length -1)
            return;
        spans[index].minutes = minutes;
        spans[index].color_index = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void readParcel(Parcel in){
        maximum = in.readInt();
        time_left = in.readInt();
        //calendar = in.readParcelable(Calendar.class.getClassLoader());
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
