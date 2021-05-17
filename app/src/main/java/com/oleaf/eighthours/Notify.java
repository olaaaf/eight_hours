package com.oleaf.eighthours;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.atomic.AtomicBoolean;

public class Notify extends Service {
    //Array of colors defined in the resources
    private TypedArray colors;
    //The current playing activity
    public Span span;

    private Thread updateThread;
    private NotificationManagerCompat manager;
    private NotificationChannel channel;
    //An Atomic Boolean for use inside a Thread
    private AtomicBoolean running = new AtomicBoolean(true);
    private NotificationCompat.Builder builder;

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
            channel = new NotificationChannel(channelId, channelName, channelImportance);
            channel.setDescription(channelDesc);
            manager.createNotificationChannel(channel);
        }
        //Set the atomic boolean to true
        running.set(true);
        updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(running.get()){
                    try{
                        Thread.sleep(500);
                    }catch(InterruptedException ie){
                        Thread.currentThread().interrupt();
                    }
                    update();
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Get a TypedArray from the resources
        colors = getResources().obtainTypedArray(R.array.colors);
        //Get the span object from the intent
        span = intent.getParcelableExtra("span");
        //Create a base notification
        builder = buildNotification(span);

        Intent notificationIntent = new Intent(this, Home.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);
        startForeground(notificationID, builder.build());

        //Start the thread that updates the notification
        updateThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        running.set(false);
        hideNotification();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void update(){
        builder.setContentText(Tools.timeMinutes(span.getMinutes() - span.getCurrentMinutes()) + " left");
        manager.notify(notificationID, builder.build());
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
}
