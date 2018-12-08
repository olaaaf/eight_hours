package com.oleaf.eighthours;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class List extends AppCompatActivity {
    Activities activities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getExtras();
        Log.d("activities", ""+activities.getSpans());
    }

    private void getExtras(){
        Bundle bundle = getIntent().getExtras();
        activities = bundle.getParcelable("activities");
    }
}
