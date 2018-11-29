package com.oleaf.eighthours;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import static java.util.Arrays.copyOf;

public class Home extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final int grid = 20;
    public static final float button_bounds = 0.1f;
    public int maximum = 60 * 8;
    public int time_left = maximum;
    private Span[] spans;
    private TextView hoursText, desc, cancel_button, confirm_button;
    private Animation popUpCircle, popUpMenu, downCircle, downMenu;
    private Menu menu;
    private boolean menuUp;
    private int color_pressed, color_normal;
    private Circle circle;
    private NumberIndicatorText indicator;
    private boolean colorChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        spans = new Span[0];
        color_normal = ContextCompat.getColor(this, R.color.cancel); color_pressed = ContextCompat.getColor(this, R.color.cancel_pressed);

        hoursText = findViewById(R.id.textView);
        menu = findViewById(R.id.menu_view);
        desc = findViewById(R.id.textView3);
        circle = findViewById(R.id.circle);
        cancel_button = findViewById(R.id.cancel);
        confirm_button = findViewById(R.id.confirm);
        indicator = findViewById(R.id.indicator);
        Spinner menu = findViewById(R.id.drop_down);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.menu_buttons, R.layout.menu_item);
        adapter.setDropDownViewResource(R.layout.menu_dropdown);
        menu.setAdapter(adapter);
        menu.setOnItemSelectedListener(this);

        popUpCircle = AnimationUtils.loadAnimation(this, R.anim.circle_up);
        popUpMenu = AnimationUtils.loadAnimation(this, R.anim.menu_popup);
        downCircle = AnimationUtils.loadAnimation(this, R.anim.circle_down);
        downMenu = AnimationUtils.loadAnimation(this, R.anim.menu_down);
        // TODO: check which solution is better performance-wise
        cancel_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    if (viewContains(cancel_button, event.getX(), event.getY())){
                        cancel();
                    }
                    cancel_button.setTextColor(color_normal);
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN){
                    cancel_button.setTextColor(color_pressed);
                }
                return true;
            }
        });
        confirm_button.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    if(viewContains(confirm_button, event.getX(), event.getY())){
                        confirm();
                    }
                    confirm_button.setTextColor(color_normal);
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN){
                    confirm_button.setTextColor(color_pressed);
                }
                return true;
            }
        });
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (v.getId() == R.id.cancel){
                        cancel_button.setTextColor(color_pressed);
                    }else{
                        confirm_button.setTextColor(color_pressed);
                    }
                }else{
                    if (v.getId() == R.id.cancel){
                        cancel_button.setTextColor(color_normal);
                    }else{
                        confirm_button.setTextColor(color_normal);
                    }
                }
            }
        };
        confirm_button.setOnFocusChangeListener(onFocusChangeListener);
        cancel_button.setOnFocusChangeListener(onFocusChangeListener);
    }
    public Span[] getSpans(){
        return spans;
    }
    public void newActivity(int minutes, int color_index){
        if (minutes < grid/2f){
            return;
        }
        int ix = 0;//findColor(color_index)+1;
        Span[] cp = new Span[spans.length+1];
        indicator.update(cp.length);
        if (ix == 0){
            spans = copyOf(spans, spans.length+1);
            spans[spans.length-1] = new Span(minutes, color_index);
            return;
        }
        for (int i = 0; i < ix; ++i) cp[i] = spans[i];
        cp[ix] = new Span(minutes, color_index);
        for (int i = ix+1; i < spans.length+1; ++i) cp[i] = spans[i - 1];
        spans = cp.clone();
    }
    //TODO: delete activity
    private int findColor(int search){
        for (int ix = spans.length-1; ix >= 0; --ix){
            if (spans[ix].color_index == search){
                return ix;
            }
        }
        return -1;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String p = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, p, Toast.LENGTH_SHORT);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class Span{
        float minutes;
        byte color_index;
        Span(int _minutes, int _color_index){
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
    public boolean isMenuUp(){
        return menuUp;
    }
    public void popup(){
        menuUp = true;
        menu.popUp();
        animation(true);
    }
    public void confirm(){
        if (!menuUp || !colorChosen)
            return;
        menuUp = false;
        circle.confirm();
        animation(false);
    }
    public void cancel(){
        if (!menuUp)
            return;
        menuUp = false;
        circle.cancel();
        animation(false);
    }
    private void animation(boolean show){
        if (show){
            cancel_button.setVisibility(View.VISIBLE);
            cancel_button.startAnimation(popUpMenu);
            menu.startAnimation(popUpMenu);
            desc.setText(R.string.drag_to_edit);
        }else{
            if (getChosen() != -1)
                confirm_button.startAnimation(downMenu);
            menu.popUp();
            colorChosen = false;
            cancel_button.startAnimation(downMenu);
            menu.startAnimation(downMenu);
            desc.setText(R.string.drag_to_add);
        }
    }
    public void chosenChanged(){
        circle.invalidate();
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
        hoursText.setText(text, 0, characters);
    }
    public int getChosen(){
        return menu.getChosen();
    }
    public boolean isAnimationDone(){
        return circle.getAnimation().hasEnded();
    }
    /**
     * @param x ought to be the local position derived form the view
     * @param y ought to be the local position derived form the view
     */
    public static boolean viewContains(View view, float x, float y, float ... bounds){
        int width = view.getWidth(); int height = view.getHeight();
        if (bounds.length > 0){
            return !(x < -width * bounds[0]) && !(y < -height * bounds[0]) && !(x > width * (1f + bounds[0])) && !(y > height * (1 + bounds[0]));
        }else
            return !(x < -width * button_bounds) && !(y < -height * button_bounds) && !(x > width * (1f + button_bounds)) && !(y > height * (1 + button_bounds));
    }
    public void colorChosen(){
        colorChosen = true;
        confirm_button.setVisibility(View.VISIBLE);
        confirm_button.startAnimation(popUpMenu);
    }
}
