package com.oleaf.eighthours.settings;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.oleaf.eighthours.R;

public class Settings extends AppCompatActivity {
    LinearLayout settingsView;
    boolean mPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (Build.VERSION.SDK_INT >= 27){
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.appWhite));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.appWhite));
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR |View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR   );
        }
        settingsView = findViewById(R.id.settingsList);
    }

    private void loadSettings(){

    }
}