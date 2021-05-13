package com.oleaf.eighthours;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notify {
    private TypedArray colors;

    private Context context;
    private NotificationManagerCompat manager;
    private NotificationChannel channel;

    private static final String channelId = "1";
    private static final String channelName = "Activities timer";
    private static final String channelDesc = "Notifications for playing the activities";
    private static final int channelImportance =  NotificationManager.IMPORTANCE_DEFAULT;

    public Notify(Context c){
        manager = NotificationManagerCompat.from(c);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelId, channelName, channelImportance);
            channel.setDescription(channelDesc);
            manager.createNotificationChannel(channel);
        }
        context = c;
        colors = c.getResources().obtainTypedArray(R.array.colors);
    }

    public void showNotification(Span span){
        int color = colors.getColor(span.color_index, 0);
        NotificationCompat.Builder builder = newBuilder()
                .setContentTitle(span.name)
                .setSubText(Tools.timeMinutes(span.getMinutes() - span.getCurrentMinutes()) + " left")
                .setColor(color)
                .setSmallIcon(R.drawable.play_na);
        manager.notify(1, builder.build());
    }

    public void hideNotification(){
        manager.cancelAll();
    }

    private NotificationCompat.Builder newBuilder(){
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder = new NotificationCompat.Builder(context, channelId);
        }else{
            builder = new NotificationCompat.Builder(context);
        }
        return builder;
    }
}
