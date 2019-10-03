package com.oleaf.eighthours.date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 This class is used to show the DatePicker Fragment
 */
public class CalendarDatePicker extends DialogFragment {
    private Calendar date;
    private EightCalendar eightCalendar;

    public static CalendarDatePicker newInstance(Calendar date, EightCalendar eightCalendar){
        CalendarDatePicker c = new CalendarDatePicker();
        c.date = date;
        c.eightCalendar = eightCalendar;
        return c;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        if (date == null){
            date = c;
        }
        return new DatePickerDialog(getActivity(), dateSetListener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    eightCalendar.changeDate(year, month, dayOfMonth);
                    Toast.makeText(getActivity(), "Selected date: " + view.getDayOfMonth() +
                            " / " + (view.getMonth()+1) +
                            " / " + view.getYear(), Toast.LENGTH_SHORT).show();
                }
            };
}