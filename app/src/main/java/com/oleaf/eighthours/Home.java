package com.oleaf.eighthours;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Home extends AppCompatActivity {
    public static final int grid = 20;
    public int maximum = 60 * 8;
    public int time_left = maximum;
    public Span[] spans;
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textView = findViewById(R.id.textView);
        spans = new Span[0];
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
