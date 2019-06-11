package com.oleaf.eighthours;

import android.os.Parcel;
import android.os.Parcelable;

import static com.oleaf.eighthours.Activities.grid;

public class Span implements Parcelable {
    public static final String default_name="Activity";
    float minutes;
    long startTime=-1, pauseTime=-1, beforePause=0;
    boolean onGoing;
    byte color_index;
    String name;

    Span(int _minutes, int _color_index, int time_left, String _name){
        minutes = clamp(round_up(_minutes), time_left);
        color_index = (byte) _color_index;
        name = _name;
    }
    Span(int _minutes, int _color_index, int time_left){
        this(_minutes, _color_index, time_left, default_name);
    }
    private int round_up(int time) {
        return Math.round(time / (float) grid) * grid;
    }
    //no built in clamp function
    private int clamp(int time, int time_left){
        if (time > time_left){
            time = time_left;
        }else if(time < 0){
            time = 0;
        }

        return time;
    }
    protected Span(Parcel in) {
        minutes = in.readFloat();
        color_index = in.readByte();
        name = in.readString();
        startTime = in.readLong();
        boolean a[] = new boolean[1];
        in.readBooleanArray(a);
        onGoing = a[0];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(minutes);
        dest.writeByte(color_index);
        dest.writeString(name);
        dest.writeLong(startTime);
        dest.writeBooleanArray(new boolean[]{onGoing});
    }

    public float getPart(){
        return getCurrentMinutes() / minutes;
    }

    public String getName(){
        return name;
    }

    public int getColorIndex(){
        return color_index;
    }

    public float getMinutes(){
        return minutes;
    }

    public float getCurrentMinutes(){
        if (startTime < 0)
            return beforePause/ 60000f;
        return (System.currentTimeMillis() - startTime + beforePause) / 60000f;
    }

    public void start(){
        onGoing = true;
        startTime = System.currentTimeMillis();
    }

    public void pause(){
        onGoing = false;
        beforePause += System.currentTimeMillis() - startTime;
        startTime = -1;
    }

    public void restart(){
        onGoing = false;
        pauseTime = -1;
        beforePause = 0;
        startTime = -1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Span> CREATOR = new Creator<Span>() {
        @Override
        public Span createFromParcel(Parcel in) {
            return new Span(in);
        }

        @Override
        public Span[] newArray(int size) {
            return new Span[size];
        }
    };
}
