package com.oleaf.eighthours;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class TextEditor extends AppCompatEditText {
    Context c;

    public TextEditor(Context context) {
        super(context);
        c =context;
    }

    public TextEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        c = context;
    }

    public TextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        c = context;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            this.clearFocus();
        }
        return super.onKeyPreIme(keyCode, event);
    }
}
