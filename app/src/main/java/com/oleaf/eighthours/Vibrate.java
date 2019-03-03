package com.oleaf.eighthours;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class Vibrate {
    public static void v(int mil, Vibrator vibrator){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createOneShot(mil, VibrationEffect.DEFAULT_AMPLITUDE));
        }else {
            vibrator.vibrate(mil);
        }
    }
}