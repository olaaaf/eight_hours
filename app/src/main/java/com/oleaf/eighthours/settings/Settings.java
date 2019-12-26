package com.oleaf.eighthours.settings;

import android.content.Context;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.oleaf.eighthours.R;
import com.oleaf.eighthours.TextEditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Settings extends AppCompatActivity {
    public static final String fileName = "settings";
    LinearLayout settingsView;
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
        //setContentView(R.layout.activity_settings);
        if (Build.VERSION.SDK_INT >= 27) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.appWhite));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.appWhite));
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
       // settingsView = findViewById(R.id.settingsList);
        loadSettings();
    }

    private void loadSettings() {
        //TextEditor maxHours = findViewById(R.id.maxHours);
       // maxHours.setFilters(new NumberInputFilter[]{new NumberInputFilter(0,24)});
    }

    void updateAll() {

    }

    void saveAll() {
        try {
            FileOutputStream settingsFile = openFileOutput("." + fileName, Context.MODE_PRIVATE);
            ObjectOutputStream outputStream = new ObjectOutputStream(settingsFile);
            //Write variables

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
