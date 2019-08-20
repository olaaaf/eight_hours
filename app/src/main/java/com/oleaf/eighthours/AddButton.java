package com.oleaf.eighthours;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddButton extends LinearLayout {
    TextView buttonText;
    RelativeLayout iconParent;
    ImageView icon;
    String[] states;
    Drawable[] icons;

    final Runnable p = new Runnable() {
        @Override
        public void run() {
            init();
        }
    };

    public AddButton(Context context) {
        super(context);
        post(p);
    }

    public AddButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        post(p);
    }

    public AddButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        post(p);
    }

    public AddButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        post(p);
    }

    private void load(Context context){
        Resources r = context.getResources();
        states = new String[]{r.getString(R.string.add_new_activity),
                r.getString(R.string.confirm_activity),
                r.getString(R.string.play_activity)};
        icons = new Drawable[]{ContextCompat.getDrawable(context, R.drawable.add_white),
                ContextCompat.getDrawable(context, R.drawable.play_not_rounded),
                ContextCompat.getDrawable(context, R.drawable.tick)};
    }

    private void init(){
        for (int ix = 0; ix < getChildCount(); ++ix){
            View child = getChildAt(ix);
            switch (child.getId()){
                case R.id.buttonIcon:
                    icon = (ImageView) child;
                    break;
                case R.id.buttonText:
                    buttonText = (TextView) child;
                    break;
                case R.id.buttonIconParent:
                    iconParent = (RelativeLayout) child;
                    break;
            }
        }
    }

    public void setState(State state){
        icon.setImageDrawable(icons[state.getValue()]);
        buttonText.setText(states[state.getValue()]);
        /*switch (state){
            case ADDNEW:
                break;
            case CONFIRM:
                break;
            case PLAY:
                break;
        }*/
    }

    enum State{
        ADDNEW(0),CONFIRM(1),PLAY(2);

        private final int value;
        State(int v){value = v;}
        int getValue() { return value; }
    }
}
