package com.oleaf.eighthours;
public abstract class BasicAnimation {
    private long startTime;
    private long duration;
    private float value;
    private float startValue;

    BasicAnimation(long duration){
        this(duration, 1f);
    }

    BasicAnimation(long duration, float value){
        this(duration, value, 0);
    }

    BasicAnimation(long duration, float value, float startValue){
        this.duration = duration;
        this.value = value;
        startTime = System.currentTimeMillis();
        this.startValue = startValue;
    }

    public void startNew(long duration, float value, float startValue) {
        this.duration = duration;
        this.value = value;
        startTime = System.currentTimeMillis();
        this.startValue = startValue;
    }

    float getValue(){
        animationFinished();
        return startValue + value * Tools.clamp((System.currentTimeMillis() - startTime) / (float) duration, 0f, 1f);
    }

    boolean animationFinished(){
        if (System.currentTimeMillis() - startTime > duration){
            afterFinish();
            return true;
        }
        return false;
    }

    public abstract void afterFinish();

}
