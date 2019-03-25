package com.oleaf.eighthours;

import android.content.Context;
import java.io.File;
import java.util.Date;
import static android.text.format.DateFormat.format;

/*
    Used for loading file configurations, and changing date
    saved under the name save_name
 */
public class Calendar {
    public static final String save_name = ".date_saves";
    public java.util.Calendar calendar;
    Context context;
    File saveFile;
    Calendar(Context context){
        this.context = context;
        calendar = java.util.Calendar.getInstance();
        //if (dateExists(calendar.getTime()) > -1){

        //}
    }
    private int dateExists(Date date){
        String[] ls = context.fileList();
        for (int ix = 0; ix < ls.length; ++ix){
            if (ls[ix].contains(save_name + convertString(date)))
                return ix;
        }
        return -1;
    }
    private String convertString(Date date){
        return format("dd", date) + "." + format("MM", date) + "." + format("yy", date);
    }
    private Date convertDate(String date){
        //TODO: Exception
        return new Date(Integer.parseInt(date.charAt(6) + "" + date.charAt(7)), Integer.parseInt(date.charAt(4) +""+ date.charAt(3)), Integer.parseInt(date.charAt(1) + "" + date.charAt(2)));
    }
}
