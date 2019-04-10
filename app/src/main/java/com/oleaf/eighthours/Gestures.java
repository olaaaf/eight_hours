package com.oleaf.eighthours;

import android.view.MotionEvent;

public abstract class Gestures{
    public static float def_difference=64;
    public static long def_longpress=400;
    private float lastX, lastY;
    private float dif;
    private boolean drag;
    private long first_press;
    private long long_press;
    //TODO private float drag_alpha;

    public Gestures(float dif){
        this.dif = dif;
        lastX = -999;
        lastY = -999;
        long_press = -1;
    }
    public Gestures(float dif, long long_press){
        this.dif = dif;
        lastX = -999;
        lastY = -999;
        this.long_press = long_press;
    }

    public void touchEvent(MotionEvent event){
        float newX = event.getX();float newY = event.getY();
        if (lastX < -990) {
            lastX = newX;
            lastY = newY;
            start();
            return;
        }
        float currentDifference = (float)Math.sqrt(Math.pow(newX-lastX, 2) + Math.pow(newY-lastY, 2));
        if (event.getActionMasked() == MotionEvent.ACTION_UP){
            if (drag){
                onDragStop(newX, newY);
            }else{
                if (timePassed()){
                    longPressStop(newX, newY);
                }else{
                    onTap(newX, newY);
                }
            }
            currentDifference = 0;
            drag = false;
            lastY = -999;
            lastX = -999;
            stop();
            return;
        }
        //dragArc and long Press detection
        if ((currentDifference >= dif || drag) && !timePassed()){
            onDrag(newX, newY);
            drag = true;
            stop();
        }else if (timePassed()){
            longPress(newX, newY);
        }
    }


    private void start(){
        first_press = System.currentTimeMillis();
    }
    private void stop(){
        first_press = -1;
    }
    private boolean timePassed(){
        return (System.currentTimeMillis() - first_press >= long_press && long_press > -1 && first_press > -1);
    }
    protected abstract void onTap(float x, float y);
    protected abstract void onDragStop(float x, float y);
    protected abstract void onDrag(float x, float y);
    protected abstract void longPress(float x, float y);
    protected abstract void longPressStop(float x, float y);

    //TODO: protected abstract void dragCircular(float x, float y)
    public boolean down(){
        return (first_press >= 0);
    }
}


