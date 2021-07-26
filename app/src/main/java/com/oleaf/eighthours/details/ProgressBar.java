package com.oleaf.eighthours.details;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.oleaf.eighthours.R;
import com.oleaf.eighthours.Tools;

import java.util.concurrent.atomic.AtomicBoolean;

public class ProgressBar extends View {
    public static final float maxAlpha=0.65f;
    private float radius, width, minPart, progress;
    private AtomicBoolean onGoing;
    private Thread thread;
    private int progressColor;
    private static final int animationSpeed = 4000;     //1 cycle per per animationSpeed
    private static final float max = 0.6f;
    private long animationStart = -1;
    private float anim=0f;
    private int primaryColor;
    private Paint paint = new Paint();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            onGoing.set(true);
            while(onGoing.get()){
                try{
                    thread.sleep(18);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
                ProgressBar.super.post(new Runnable() {
                    @Override
                    public void run() {
                        anim = ((System.currentTimeMillis() - animationStart) / (float) animationSpeed) % (max * 2f);
                        if (anim > max)
                            anim = max * 2f - anim;
                        invalidate();
                    }
                });
            }
        }
    };

    public ProgressBar(Context context) {
        super(context);
        init(context);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context c){
        paint.setAntiAlias(true);
        progressColor = ContextCompat.getColor(c, R.color.progress_color);
        onGoing = new AtomicBoolean(false);
        post(new Runnable() {
            @Override
            public void run() {
                radius = getHeight() / 2f;
                width = getWidth();
                minPart = getHeight() / ((float) getWidth() * 2);
                updateProgress(minPart);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas){
        //Draw the background bar for reference
        paint.setColor(progressColor);
        drawBar(canvas, 1f);
        //Draw the animation
        progressAnimation(canvas, Tools.clamp(logisticFunction(anim),0f, 1f));
        //Draw the actual progress
        paint.setColor(primaryColor);
        drawBar(canvas, progress);
    }

    private void drawBar(Canvas canvas, float part){
        if (part == 0)
            return;
        if (part <= minPart){
            canvas.drawCircle(radius, radius, radius, paint);
        }
        else{
            canvas.drawCircle(radius, radius, radius, paint);
            canvas.drawRect(radius, 0, part * (width - 1 * radius), 2 * radius, paint);

            if (part < 0.99f){
                canvas.drawCircle( part * (width - 1 *radius), radius, radius, paint);
            }else{
                canvas.drawArc(part * (width - 2 *radius), 0, part * (width),radius * 2f, -90, 180, true, paint);
            }
        }
    }

    private void progressAnimation(Canvas canvas, float alpha){
        paint.setColor(((int) (alpha * 255) << 24) | (0x00FFFFFF & primaryColor));
        //Log.d("Ayy", onGoing+" "+anim);

        drawBar(canvas, 1f);
    }

    public void setColor(int color){
        primaryColor = color;
    }

    public void updateProgress(float progress){
        this.progress = progress;
        invalidate();
    }

    public float logisticFunction(float in){
        return 1f / (1f + 280f * (float)Math.pow(Math.E, in * -11));
    }

    public void startProgress(){
        animationStart = System.currentTimeMillis();
        thread = new Thread(runnable);
        thread.start();
    }

    public void stopProgress(){
        onGoing.set(false);
        animationStart = -1;
        anim = 0f;
        invalidate();
    }
}
