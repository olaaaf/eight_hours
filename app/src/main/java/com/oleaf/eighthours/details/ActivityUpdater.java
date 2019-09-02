package com.oleaf.eighthours.details;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.oleaf.eighthours.R;
import com.oleaf.eighthours.Span;
import com.oleaf.eighthours.Tools;
import com.oleaf.eighthours.details.ProgressBar;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ActivityUpdater extends View{
    private Span span;
    TextView left;
    private ProgressBar progressBar;
    private int index;
    private Thread thread;
    private AtomicBoolean running = new AtomicBoolean(false);
    private long msUpdate = 500;
    private float skipValue = 0;
    private Runnable change;
    public static final long defaultMsUpdate = 500;
    public static final long defaultMsUpdateQuick = 32;

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
        if (progressBar != null){
            progressBar.updateProgress(span.getPart());
            span.addActiveMinutes(skipValue);
            if (span.shouldStop()){
                stop();
                change.run();
                left.setText(R.string.done_timer);
            }else{
                left.setText(Tools.timeMinutes(span.getMinutes() - span.getCurrentMinutes()) + " left");
                Log.d("Time", span.getCurrentMinutes() + "");
            }
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

    public void init(Span span, ProgressBar progressBar, TextView left, Runnable changeDrawable){
        this.span = span;
        this.progressBar = progressBar;
        this.left = left;
        this.change = changeDrawable;
    }

    private void interrupt(){
        stop();
        thread.interrupt();
    }

    public void start(){
        skipValue = 0;
        thread = new Thread(runnable);
        thread.start();
        span.start();
    }

    public void stop(){
        running.set(false);
        span.pause();
    }

    public void setMsUpdate(long msUpdate){
        if (msUpdate <= 0)
            this.msUpdate = defaultMsUpdate;
        else
            this.msUpdate = msUpdate;
    }

    /**
     * @param min minutes per second
     */
    public void startSkipping(float min){
        setMsUpdate(defaultMsUpdateQuick);
        if (min < 0 && !isRunning()){
            change.run();
            start();
        }
        skipValue = min;
    }

    public void stopSkipping(){
        setMsUpdate(defaultMsUpdate);
        skipValue = 0;
    }

    public boolean isRunning(){
        return running.get();
    }
}