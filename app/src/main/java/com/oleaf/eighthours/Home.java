package com.oleaf.eighthours;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Home extends AppCompatActivity {
    public static final int grid = 20;
    public int maximum = 60 * 8;
    public int time_left = maximum;
    public Span[] spans;
    public TextView textView, desc, cancel;
    public Menu menu;
    private Animation popUpCircle, popUpMenu, downCircle, downMenu;
    public int color_pressed, color_normal;
    private Circle circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textView = findViewById(R.id.textView);
        spans = new Span[0];
        menu = findViewById(R.id.menu_view);
        desc = findViewById(R.id.textView3);
        circle = findViewById(R.id.circle);
        cancel = findViewById(R.id.cancel);
        color_normal = ContextCompat.getColor(this, R.color.cancel); color_pressed = ContextCompat.getColor(this, R.color.cancel_pressed);

        popUpCircle = AnimationUtils.loadAnimation(this, R.anim.circle_up);
        popUpMenu = AnimationUtils.loadAnimation(this, R.anim.menu_popup);
        downCircle = AnimationUtils.loadAnimation(this, R.anim.circle_down);
        downMenu = AnimationUtils.loadAnimation(this, R.anim.menu_down);

        cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    //TODO: Canceling caneling :) [check if pointer position is on text]
                    cancel();
                    cancel.setTextColor(color_normal);
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN){
                    cancel.setTextColor(color_pressed);
                }
                return true;
            }
        });
    }
    public Span[] getSpans(){
        return spans;
    }
    public void newActivity(int minutes, int color_index){
        if (minutes < grid/2f){
            return;
        }
        int ix = findColor(color_index)+1;
        Span[] cp = new Span[spans.length+1];
        for (int i = 0; i < ix; ++i) cp[i] = spans[i];
        cp[ix] = new Span(minutes, color_index);
        for (int i = ix+1; i < spans.length+1; ++i) cp[i] = spans[i - 1];
        spans = cp.clone();
    }
    private int findColor(int search){
        for (int ix = spans.length-1; ix >= 0; --ix){
            if (spans[ix].color_index == search){
                return ix;
            }
        }
        return -1;
    }
    public class Span{
        float minutes;
        byte color_index;
        public Span(int _minutes, int _color_index){
            minutes = clamp(round_up(_minutes));
            color_index = (byte) _color_index;
            time_left -= minutes;
        }
        private int round_up(int time) {
            return Math.round(time / grid) * grid;
        }
        //no built in clamp function
        private int clamp(int time){
            if (time > time_left){
                time = time_left;
            }else if(time < 0){
                time = 0;
            }

            return time;
        }
    }
    public void popup(){
        cancel.setVisibility(View.VISIBLE);
        menu.popUp();
        circle.startAnimation(popUpCircle);
        cancel.startAnimation(popUpMenu);
        menu.startAnimation(popUpMenu);
        desc.startAnimation(popUpCircle);
        desc.setText(R.string.drag_to_edit);
        textView.startAnimation(popUpCircle);
    }

    public void cancel(){
        circle.cancel();
        cancel.startAnimation(downMenu);
        circle.startAnimation(downCircle);
        menu.startAnimation(downMenu);
        desc.setText(R.string.drag_to_add);
        desc.startAnimation(downCircle);
        textView.startAnimation(downCircle);
    }
    public void updateText(int minutes){
        int hours = (int) Math.floor((float)minutes/60f);
        minutes = minutes - (hours*60);
        char text[];
        int characters = 13;

        char x = '\0';
        if ((int)Math.floor(minutes/10f) != 0) {
            x = Character.forDigit((int) Math.floor(minutes / 10f), 5);
        }
        if (hours != 1){
            text = new char[]{Character.forDigit(hours, 24), ' ', 'h', 'o', 'u', 'r',  's', '\n',
                    x, Character.forDigit(minutes%10, 9),' ', 'm', 'i', 'n'};
            characters++;

        }else{
            text = new char[]{Character.forDigit(hours, 24), ' ', 'h', 'o', 'u', 'r', '\n',
                    x, Character.forDigit(minutes%10, 9), ' ', 'm', 'i', 'n'};
        }
        textView.setText(text, 0, characters);
    }
}
