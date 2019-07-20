package com.oleaf.eighthours.menu;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.oleaf.eighthours.Activities;
import com.oleaf.eighthours.R;

public class Menu extends AppCompatActivity {
    public RecyclerView list;
    public ActivityAdapter aAdapter;
    public Activities activities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        list = findViewById(R.id.activity_list);

        Intent intent = getIntent();
        activities = (Activities) intent.getParcelableExtra("activities");
        TypedArray colors = getResources().obtainTypedArray(R.array.colors);
        ActivityUpdater updater = findViewById(R.id.updater);
        aAdapter = new ActivityAdapter(activities, colors, updater);
        RecyclerView recyclerView = findViewById(R.id.activity_list);
        recyclerView.setAdapter(aAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (recyclerView.getAdapter().getItemCount() < 1) {
            findViewById(R.id.textView5).setVisibility(View.VISIBLE);
        }
    }

    public void listBack(View view){
        Intent intent = new Intent();
        intent.putExtra("activities", aAdapter.activities);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("activities", aAdapter.activities);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
