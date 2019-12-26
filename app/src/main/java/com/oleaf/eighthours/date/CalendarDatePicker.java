package com.oleaf.eighthours.date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 This class is used to show the DatePicker Fragment
 */
public class CalendarDatePicker extends DialogFragment {
    private Calendar date;
    private EightCalendar eightCalendar;
    private DialogInterface.OnClickListener todayListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            eightCalendar.setToday();
        }
    };

    public static CalendarDatePicker newInstance(Calendar date, EightCalendar eightCalendar){
        CalendarDatePicker c = new CalendarDatePicker();
        c.date = date;
        c.eightCalendar = eightCalendar;
        return c;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Get today's date
        final Calendar c = Calendar.getInstance();
        if (date == null){
            date = c;
        }
        //initlialize DatePickerDialog
        DatePickerDialog d = new DatePickerDialog(getActivity(), dateSetListener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        //Add Today button with todayListener onClickListener
        d.setButton(DialogInterface.BUTTON_NEUTRAL, "Today", todayListener);
        return d;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    eightCalendar.changeDate(year, month, dayOfMonth);

                }
            };
}