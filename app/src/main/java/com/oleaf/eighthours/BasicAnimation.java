package com.oleaf.eighthours;

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
        return value * Tools.clamp((System.currentTimeMillis() - startTime) / duration, 0f, 1f);
    }
}
