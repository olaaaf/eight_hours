package com.oleaf.eighthours;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import com.oleaf.eighthours.menu.Menu;

public final class ChangingActivity {
    static void change(Context current, String currentName, String name, Parcelable activities){
        if (currentName.matches(name))
            return;
        Intent intent = new Intent();
        switch (name){
            default:
                return;

            case "Circle":
                intent = new Intent(current, Circle.class);
                break;
            case "Menu":
                intent = new Intent(current, Menu.class);
                break;
        }
        intent.putExtra("activities", activities);
        current.startActivity(intent);
    }
}
