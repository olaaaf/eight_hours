package com.oleaf.eighthours.calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import com.oleaf.eighthours.R;

public class Calendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (0.9f * displayMetrics.widthPixels);
        getWindow().setLayout(width, width);
        setContentView(R.layout.activity_calendar);
    }
}
