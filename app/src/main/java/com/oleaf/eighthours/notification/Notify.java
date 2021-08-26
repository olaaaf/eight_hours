package com.oleaf.eighthours.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.oleaf.eighthours.Home;
import com.oleaf.eighthours.R;
import com.oleaf.eighthours.Span;
import com.oleaf.eighthours.Tools;
import com.oleaf.eighthours.date.EightCalendar;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

public class Notify extends Service {
    //Array of colors defined in the resources
    private TypedArray colors;
    //The current playing activity
    public Span span;

    private Thread updateThread;
    private NotificationManagerCompat manager;
    //An Atomic Boolean for use inside a Thread
    private AtomicBoolean running = new AtomicBoolean(true);
    private AtomicBoolean hide = new AtomicBoolean(false);
    private NotificationCompat.Builder builder;
    private Calendar date;

    private final MyBinder myBinder = new MyBinder();
    //Notification constants
    private static final String channelId = "1";
    private static final String channelName = "Activities timer";
    private static final String channelDesc = "Notifications for playing the activities";
    private static final int channelImportance =  NotificationManager.IMPORTANCE_DEFAULT;
    private static final int notificationID = 2137;

    @Override
    public void onCreate() {
        super.onCreate();
        manager = NotificationManagerCompat.from(this);
        //Create a notification channel if the Android version is newer or equal to O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, channelImportance);
            channel.setDescription(channelDesc);
            manager.createNotificationChannel(channel);
        }
        //Set the atomic boolean to true
        running.set(true);
        updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(running.get()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    if (!hide.get()) {
                        update();
                    } else {
                        hideNotification();
                    }
                }
            }
        });
    }

    /**
     * Attach a span to the notification and start the runnable thread
     * @param s - the span to attach to
     */
    public void attachSpan(Span s, Calendar date){
        //Obtain the date of current span (used for saving progress later)
        this.date = date;
        //Get a TypedArray from the resources
        colors = getResources().obtainTypedArray(R.array.colors);
        //Create a base notification
        this.span = s;
        hide.set(false);
        builder = buildNotification(span);
        Intent notificationIntent = new Intent(this, Home.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);
        startForeground(notificationID, builder.build());
    }

    /**
     * Detach the span from the notification, hide the notification and stop the runnable thread, while keeping the service going
     */
    public void detach(){
        hide.set(true);
        if (date != null && span != null)
            //Save span
            EightCalendar.saveSpan(span, this, date);
        //Hide notification
        hideNotification();
    }

    public Span getSpan(){
        return span;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        running.set(true);
        //Start the thread that updates the notification
        if (!updateThread.isAlive())
            updateThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        running.set(false);
        detach();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public int getRandom(){
        return (int) (Math.random() * 100f);
    }

    public void update(){
        if (span != null && running.get()){
            builder.setContentText(Tools.timeMinutes(span.getMinutes() - span.getCurrentMinutes()) + " left");
            manager.notify(notificationID, builder.build());
        }else{
            hideNotification();
        }


    }

    //Notification functions

    public NotificationCompat.Builder buildNotification(Span span){
        //Set colors and the properties of the notification
        int color = colors.getColor(span.color_index, 0);
        NotificationCompat.Builder builder = newBuilder()
                .setContentTitle(span.name)
                .setContentText(Tools.timeMinutes(span.getMinutes() - span.getCurrentMinutes()) + " left")
                .setColor(color)
                .setNotificationSilent()
                .setTicker(getText(R.string.notification_ticker))
                .setSmallIcon(R.drawable.play_na);
        return builder;
    }

    private void hideNotification(){
        manager.cancel(notificationID);
        //save span progress
        stopForeground(true);
    }

    private NotificationCompat.Builder newBuilder(){
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder = new NotificationCompat.Builder(this, channelId);
        }else{
            builder = new NotificationCompat.Builder(this);
        }
        return builder;
    }

    public class MyBinder extends Binder {
        Notify getService() {
            // Return this instance of MyService so clients can call public methods
            return Notify.this;
        }
    }
}
