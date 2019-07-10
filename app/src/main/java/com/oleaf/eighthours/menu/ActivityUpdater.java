package com.oleaf.eighthours.menu;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ActivityUpdater extends View{
    private ArrayList<Boolean> calls = new ArrayList<>(0);
    public ArrayList<ActivityConstraint> activities = new ArrayList<>(0);
    private Thread thread;
    private AtomicBoolean running = new AtomicBoolean(false);
    final static long msUpdate = 1000;
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            running.set(true);
            while(running.get()){
                try{
                    Thread.sleep(msUpdate);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
                update();
            }
        }
    };

    public void update(){
        for (int x = 0; x < calls.size(); ++x){
            if (calls.get(x) && activities.size() > x)
                activities.get(x).update();
        }
    }

    public ActivityUpdater(Context context) {
        super(context);
    }

    public ActivityUpdater(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ActivityUpdater(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ActivityUpdater(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(){

    }

    public void addCall(int position, ActivityConstraint c){
        if (position >= activities.size()){
            calls.add(Boolean.FALSE);
            activities.add(c);
        }
    }

    public void resetCalls(){
        calls = new ArrayList<>(0);
        activities = new ArrayList<>(0);
    }

    private void interrupt(){
        stop();
        thread.interrupt();
    }

    private void start(){
        thread = new Thread(runnable);
        thread.start();
    }

    private void stop(){
        running.set(false);
    }

    public void start(int position){
        calls.set(position, Boolean.TRUE);
        if (!running.get())
            start();
    }

    public void stop(int position){
        calls.set(position, Boolean.FALSE);
        if (check())
            interrupt();
    }

    private boolean check(){
        for (Boolean b : calls){
            if (b)
                return false;
        }
        return true;
    }

}