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
                break;
            case "Menu":
                intent = new Intent(current, Menu.class);
                break;
        }

        current.startActivity(intent);
    }
}
