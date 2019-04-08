package com.oleaf.eighthours;

import android.util.Log;

public class BasicAnimation {
    long startTime;
    long duration;
    float value;

    BasicAnimation(long duration){
        this(duration, 1f);
    }

    BasicAnimation(long duration, float value){
        this.duration = duration;
        this.value = value;
        startTime = System.currentTimeMillis();
    }

    float getValue(){
        Log.println(Log.DEBUG, "value", "" + (value * Tools.clamp((System.currentTimeMillis() - startTime) / (float) duration, 0f, 1f)));
        return value * Tools.clamp((System.currentTimeMillis() - startTime) / (float) duration, 0f, 1f);
    }

    boolean animationFinished(){
        return (System.currentTimeMillis() - startTime > duration);
    }

}
