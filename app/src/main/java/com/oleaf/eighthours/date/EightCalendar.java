package com.oleaf.eighthours.date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
    This class is used to load and save files (dates) with Activities object in Home
    Also used for changing the dates and showing the DatePicker
 */
public class EightCalendar {
    Calendar date;

    public EightCalendar(){
        date = Calendar.getInstance();
    }

    public void showDatePicker(FragmentManager supportedFragmentManager){
        DialogFragment fragment = CalendarDatePicker.newInstance(date, this);
        fragment.show(supportedFragmentManager, "DatePicker");
    }

    public void changeDate(int year, int month, int day){
        date.set(year, month, day);
    }
}
