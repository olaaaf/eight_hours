package com.oleaf.eighthours;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class TextEditor extends AppCompatEditText {
    Context context;
    Runnable clearFocus = new Runnable() {
        @Override
        public void run() {
            try{
                Thread.sleep(40);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
            clearFocus();
        }
    };
    Thread t = new Thread(clearFocus);

    public TextEditor(Context context) {
        super(context);
    }

    public TextEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.FLAG_EDITOR_ACTION){
            this.clearFocus();
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
