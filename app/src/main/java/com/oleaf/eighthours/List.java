package com.oleaf.eighthours;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;

public class List extends AppCompatActivity {
    Activities activities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getExtras();
        RecyclerView recyclerView = findViewById(R.id.list_act);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void getExtras(){
        Bundle bundle = getIntent().getExtras();
        activities = bundle.getParcelable("activities");
    }

    private String[] getActivities(){
        java.util.List act = new ArrayList();
        Span[] spans = activities.getSpans();
        for (int ix = 0; ix < spans.length; ++ix){
            if (spans[ix].name.isEmpty())
                act.add("activity " + ix);
            else
                act.add(spans[ix].name);
        }
        return (String[]) act.toArray();
    }


}
