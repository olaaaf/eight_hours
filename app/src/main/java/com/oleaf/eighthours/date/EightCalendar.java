package com.oleaf.eighthours.date;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import com.oleaf.eighthours.Activities;
import com.oleaf.eighthours.Home;

import java.io.*;
import java.util.Calendar;

/**
    This class is used to load and save files (dates) with Activities object in Home
    Also used for changing the dates and showing the DatePicker
 */
public class EightCalendar {
    private static final String dirName = "dates";
    Home home;
    Calendar date;
    Activities activities;
    Context context;

    public EightCalendar(Activities activities, Context context, Home home){
        date = Calendar.getInstance();
        this.activities = activities;
        this.context = context;
        this.home = home;
    }

    public void showDatePicker(FragmentManager supportedFragmentManager){
        DialogFragment fragment = CalendarDatePicker.newInstance(date, this);
        fragment.show(supportedFragmentManager, "DatePicker");
    }

    public void changeDate(int year, int month, int day){
        writeDate();
        date.set(year, month, day);
        readDate();
    }

    public void saveActivities(){

    }

    private void checkFolder(){
        File folder = context.getDir(dirName, Context.MODE_PRIVATE);
        if (!folder.exists()){
            folder.mkdir();
        }
    }

    private void writeDate(){
        checkFolder();
        try{
            FileOutputStream dateFile = context.openFileOutput("."+date.get(Calendar.DAY_OF_MONTH)+"-"+date.get(Calendar.MONTH) + "-" +date.get(Calendar.YEAR), Context.MODE_PRIVATE);
            ObjectOutputStream outputStream = new ObjectOutputStream(dateFile);
            outputStream.writeObject(home.activities);
            outputStream.close();
            dateFile.close();
        }catch(IOException e){
            Log.d("IO Error", e.getMessage());
        }
    }

    public void readDate(){
        checkFolder();
        File dateFile = new File(context.getFilesDir(),"."+date.get(Calendar.DAY_OF_MONTH)+"-"+date.get(Calendar.MONTH) + "-" +date.get(Calendar.YEAR));

        home.resetActivities();
        if (dateFile.exists()){
            try{
                FileInputStream fileInputStream = new FileInputStream(dateFile);
                ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
                home.activities = (Activities) inputStream.readObject();
                inputStream.close();
                fileInputStream.close();
            }catch(IOException e){
                Log.d("IO Error", e.getMessage());
            }catch (ClassNotFoundException e){
                Log.d("Class not found", e.getMessage());
            }
        }
        home.updateActivities();
    }

}
