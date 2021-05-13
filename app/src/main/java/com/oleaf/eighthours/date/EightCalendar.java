package com.oleaf.eighthours.date;

import android.content.Context;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.res.TypedArray;
import android.util.Log;
import android.widget.TextView;

import com.oleaf.eighthours.Activities;
import com.oleaf.eighthours.Home;
import com.oleaf.eighthours.R;

import java.io.*;
import java.util.Calendar;

/**
    This class is used to load and save files (dates) with Activities object in Home
    Also used for changing the dates and showing the DatePicker
 */
public class EightCalendar {
    private static final String dirName = "dates";
    public static TypedArray dayNames;
    Home home;
    Calendar date;
    Activities activities;
    Context context;

    public EightCalendar(Activities activities, Context context, Home home){
        date = Calendar.getInstance();
        this.activities = activities;
        this.context = context;
        this.home = home;
        dayNames = context.getResources().obtainTypedArray(R.array.day_names);
    }

    public void showDatePicker(FragmentManager supportedFragmentManager){
        DialogFragment fragment = CalendarDatePicker.newInstance(date, this);
        fragment.show(supportedFragmentManager, "DatePicker");
    }

    public void changeDate(int year, int month, int day){
        writeDate();
        date.set(year, month, day);
        readDate();
        home.updateButton(isPast());
        updateDayIndicator();
    }

    public void setToday(){
        Calendar today = Calendar.getInstance();
        changeDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
    }

    public void saveActivities(){
        writeDate();
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

    private void updateDayIndicator(){
        TextView dateIndicator = home.findViewById(R.id.dateIndicator);
        if (isToday()){
            dateIndicator.setText(R.string.today);
        }else{
            dateIndicator.setText(dayNames.getText(date.get(Calendar.DAY_OF_WEEK)-1));
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
                home.changeActivities((Activities) inputStream.readObject());
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

    public boolean isToday(){
        Calendar currentDate = Calendar.getInstance();
        return (currentDate.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR) && currentDate.get(Calendar.YEAR) == date.get(Calendar.YEAR));
    }

    public boolean isPast(){
        Calendar currentDate = Calendar.getInstance();
        return !(date.get(Calendar.YEAR) > currentDate.get(Calendar.YEAR) || (date.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) && date.get(Calendar.DAY_OF_YEAR) >= currentDate.get(Calendar.DAY_OF_YEAR)));
    }
}
