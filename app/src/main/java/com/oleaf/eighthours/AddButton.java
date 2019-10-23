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
    int colorInactive;
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
        load(context);
        post(p);
    }

    public AddButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        load(context);
        post(p);
    }

    public AddButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        load(context);
        post(p);
    }

    private void load(Context context){
        Resources r = context.getResources();
        states = new String[]{r.getString(R.string.add_new_activity),
                r.getString(R.string.confirm_activity),
                r.getString(R.string.play_activity),
                r.getString(R.string.play_activity)};
        icons = new Drawable[]{ContextCompat.getDrawable(context, R.drawable.add_white),
                ContextCompat.getDrawable(context, R.drawable.tick),
                ContextCompat.getDrawable(context, R.drawable.play_na),
                ContextCompat.getDrawable(context, R.drawable.play_na)};
        colorInactive = ContextCompat.getColor(context, R.color.inactive_text);
    }

    private void init(){
        for (int ix = 0; ix < getChildCount(); ++ix){
            View child = getChildAt(ix);
            switch (child.getId()){
                case R.id.buttonIconParent:
                    iconParent = (RelativeLayout) child;
                    icon = (ImageView) iconParent.getChildAt(0);
                    break;
                case R.id.buttonTextParent:
                    buttonText = (TextView) ((RelativeLayout) child).getChildAt(0);
                    break;
            }
        }
        setState(State.ADDNEW);
    }

    public void setState(State state){
        iconParent.setVisibility(VISIBLE);
        icon.setImageDrawable(icons[state.getValue()]);
        buttonText.setText(states[state.getValue()]);
        if (state == State.PLAY_INACTIVE){
            setTint(colorInactive);
        }else{
            setTint(0xFFFFFFFF);
        }
    }

    private void setTint(int color){
        icon.setColorFilter(color);
        buttonText.setTextColor(color);
    }

    enum State{
        ADDNEW(0),CONFIRM(1),PLAY(2),PLAY_INACTIVE(3);

        private final int value;
        State(int v){value = v;}
        int getValue() { return value; }
    }
}
