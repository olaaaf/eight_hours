package com.oleaf.eighthours;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

public class Home extends AppCompatActivity {
    public static final int grid = 20;
    public static final float button_bounds = 0.1f;

    private boolean menuUp, colorChosen;
    private int color_pressed, color_normal, color_inactive;

    private TextView hoursText, desc, cancel_button, confirm_button, add_button;

    private Animation popUpMenu, downMenu;
    private Menu menu;
    private Circle circle;

    public Activities activities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getExtras())
            activities = new Activities(this);

        setContentView(R.layout.activity_home);
        color_normal = ContextCompat.getColor(this, R.color.cancel); color_pressed = ContextCompat.getColor(this, R.color.cancel_pressed);
        color_inactive = ContextCompat.getColor(this, R.color.inactive_text);
        hoursText = findViewById(R.id.textView);
        menu = findViewById(R.id.menu_view);
        desc = findViewById(R.id.textView3);
        circle = findViewById(R.id.circle);
        cancel_button = findViewById(R.id.cancel);
        confirm_button = findViewById(R.id.confirm);
        add_button = findViewById(R.id.addButton);
        popUpMenu = AnimationUtils.loadAnimation(this, R.anim.menu_popup);
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
                if (event.getAction() == MotionEvent.ACTION_UP && colorChosen){
                    if(viewContains(confirm_button, event.getX(), event.getY())){
                        confirm();
                    }
                    confirm_button.setTextColor(color_normal);
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN && colorChosen){
                    confirm_button.setTextColor(color_pressed);
                }
                return true;
            }
        });
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.addNew();
            }
        });
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (v.getId() == R.id.cancel){
                        cancel_button.setTextColor(color_pressed);
                    }else{
                        if (colorChosen)
                            confirm_button.setTextColor(color_pressed);
                    }
                }else{
                    if (v.getId() == R.id.cancel){
                        cancel_button.setTextColor(color_normal);
                    }else{
                        if (colorChosen)
                            confirm_button.setTextColor(color_normal);
                    }
                }
            }
        };
        confirm_button.setOnFocusChangeListener(onFocusChangeListener);
        cancel_button.setOnFocusChangeListener(onFocusChangeListener);
    }
    public void addActivity(int min, int color){
        activities.newActivity(min, color);
        //indicator.update(activities.getLength());
    }

    //Activity changing
    private boolean getExtras(){
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return false;
        if (bundle.size() < 1)
            return false;
        activities = bundle.getParcelable("activities");
        if (activities == null)
            activities = new Activities(this);
        return true;
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
        colorChosen = false;
        menuUp = false;
        circle.confirm();
        animation(false);
    }
    public void cancel(){
        if (!menuUp)
            return;
        colorChosen = false;
        menuUp = false;
        circle.cancel();
        animation(false);
    }
    private void animation(boolean show){
        if (show){
            confirm_button.setVisibility(View.VISIBLE);
            cancel_button.setVisibility(View.VISIBLE);
            cancel_button.startAnimation(popUpMenu);
            confirm_button.startAnimation(popUpMenu);
            confirm_button.setTextColor(color_inactive);
            menu.startAnimation(popUpMenu);
            desc.setText(R.string.drag_to_edit);
        }else{
            menu.popUp();
            colorChosen = false;
            confirm_button.startAnimation(downMenu);
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
    public void colorChosen(){
        colorChosen = true;
        confirm_button.setTextColor(color_normal);
    }
    public boolean isAnimationDone(){
        return circle.getAnimation().hasEnded();
    }
    //TODO: change activities animation
    //TODO: better activities changing
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
}
