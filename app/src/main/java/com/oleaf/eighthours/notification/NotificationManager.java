package com.oleaf.eighthours.notification;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.oleaf.eighthours.Home;
import com.oleaf.eighthours.Span;

import static android.content.Context.ACTIVITY_SERVICE;

public class NotificationManager {
    Notify service;
    public boolean isBound = false;
    //This flag is used to connect to a service that is already running on app start
    boolean isRunning;
    Context context;
    Span span;
    Runnable eachUpdate;

    public NotificationManager(Context context) {
        this.context = context;
    }

    public void stopService(){
        //Intent with Notify service
        Intent intent = new Intent(context, Notify.class);
        //Stop the service - invoke the Destroy function
        context.stopService(intent);
    }

    public void disconnect(){
        if (isBound){
            isBound = false;
            context.unbindService(serviceConnection);
            service.detach();
        }
    }

    public void connect(Span span){
        if (!isBound){
            isBound = true;
            this.span = span;
            bindService();
        }
    }

    public void startService(){
        //New intent intended to start the Notify service
        Intent intent = new Intent(context, Notify.class);
        //Stop the service just in case
        context.stopService(intent);
        //Start the service
        context.startService(intent);
    }

    private void bindService(){
        Intent serviceBindIntent =  new Intent(context, Notify.class);
        context.bindService(serviceBindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d("S","ServiceConnection: connected to service.");
            // We've bound to MyService, cast the IBinder and get MyBinder instance
            Notify.MyBinder myBinder = (Notify.MyBinder) binder;
            service = myBinder.getService();
            service.attachSpan(span, ((Home) context).eightCalendar.getDate());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("S","ServiceConnection: disconnected from service.");
        }
    };
}
