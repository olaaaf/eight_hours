package com.oleaf.eighthours;

import android.os.Parcel;
import android.os.Parcelable;

import static com.oleaf.eighthours.Activities.grid;

public class Span implements Parcelable {
    float minutes;
    byte color_index;
    String name;

    Span(int _minutes, int _color_index, int time_left){
        minutes = clamp(round_up(_minutes), time_left);
        color_index = (byte) _color_index;
        name = "";
    }
    private int round_up(int time) {
        return Math.round(time / grid) * grid;
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
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(minutes);
        dest.writeByte(color_index);
        dest.writeString(name);
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
