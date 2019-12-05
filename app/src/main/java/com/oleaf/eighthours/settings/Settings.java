package com.oleaf.eighthours.settings;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.oleaf.eighthours.Activities;
import com.oleaf.eighthours.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;

public class Settings extends AppCompatActivity {
    public static final String fileName = "settings";
    LinearLayout settingsView;
    Volatile<Boolean> mPlay;
    Runnable r = new Runnable() {
        @Override
        public void run() {
            updateAll();
            saveAll();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (Build.VERSION.SDK_INT >= 27) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.appWhite));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.appWhite));
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        settingsView = findViewById(R.id.settingsList);
        mPlay = new Volatile<>(false, r);
    }

    private void loadSettings() {
        ((Switch) findViewById(R.id.mplay)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPlay.set(isChecked);
            }
        });
    }

    class Volatile<T> implements Serializable {
        private T variable;
        private Runnable r;

        Volatile(T v, Runnable runnable) {
            variable = v;
            r = runnable;
        }

        void set(T nV) {
            variable = nV;
            r.run();
        }

        T get() {
            return variable;
        }
    }

    void updateAll() {

    }

    void saveAll() {
        try {
            FileOutputStream settingsFile = openFileOutput("." + fileName, Context.MODE_PRIVATE);
            ObjectOutputStream outputStream = new ObjectOutputStream(settingsFile);
            //Write variables
            outputStream.writeBoolean(mPlay.get());

            //Close file
            outputStream.close();
            settingsFile.close();
        } catch (IOException e) {
            Log.d("IO Error", e.getMessage());
        }
    }

    void loadAll(){
        File settingsFile = new File(getFilesDir(),"."+fileName);

        if (settingsFile.exists()){
            try{
                FileInputStream fileInputStream = new FileInputStream(settingsFile);
                ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
                //Read variables
                mPlay.set(inputStream.readBoolean());

                //Close file
                inputStream.close();
                fileInputStream.close();
            }catch(IOException e){
                Log.d("IO Error", e.getMessage());
            }
        }
        updateAll();
    }
}
