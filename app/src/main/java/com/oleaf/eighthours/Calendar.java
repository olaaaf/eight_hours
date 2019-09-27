package com.oleaf.eighthours;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
    This class is used to load and save files (dates) with Activities object in Home
    Also used for changing the dates and showing the DatePicker
 */
public class Calendar {


    /**
        This class is used to show the DatePicker Fragment
     */
    public class CalendarDatePicker extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final android.icu.util.Calendar c = android.icu.util.Calendar.getInstance();
            int year = c.get(android.icu.util.Calendar.YEAR);
            int month = c.get(android.icu.util.Calendar.MONTH);
            int day = c.get(android.icu.util.Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
        }
    }
}
