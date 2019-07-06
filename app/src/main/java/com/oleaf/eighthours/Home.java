package com.oleaf.eighthours;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class Home extends AppCompatActivity {
    public static final int grid = 20;
    public static final float button_bounds = 0.1f;

    public TextView hoursText, desc, add_button;
    private ColorMenu colorMenu;
    private ColorPick colorPick;
    private PlayMenu playMenu;
    private Options options;
    public Circle circle;
    public Activities activities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getExtras())
            activities = new Activities(this);

        setContentView(R.layout.activity_home);

        hoursText = findViewById(R.id.textView);
        colorMenu = findViewById(R.id.color_menu);
        desc = findViewById(R.id.textView3);
        circle = findViewById(R.id.circle);
        options = findViewById(R.id.options);
        add_button = findViewById(R.id.addButton);
        colorPick = findViewById(R.id.menu_view);
        playMenu = findViewById(R.id.play_menu);
    }
    public int addActivity(int min, int color){
        return activities.newActivity(min, color);
        //indicator.update(activities.getLength());
    }
    public void editActivity(int index, int min, int color){
        activities.editActivity(index, min, (byte) color);
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
    public void showStopwatch(){
        playMenu.show();
    }
    public void hideStopwatch(){
        playMenu.hide();
    }
    public boolean colorUp(){
        return colorMenu.isShown();
    }
    public void colorShow(){
        colorMenu.show();
        colorPick.noAnimation();
        desc.setText(R.string.drag_to_edit);
    }
    public void colorShow(int color_index){
        colorMenu.show(color_index);
        colorPick.noAnimation();
        desc.setText(R.string.drag_to_edit);
    }
    public void colorHide(){
        updateText(activities.time_left);
        desc.setText("");
    }
    public void updateBottom(float minutesLeft, float minutes){
        if (options.index > -1){
            desc.setText(((int)(minutesLeft/60)+":"+(int)(minutesLeft%60)+":"+(int)Math.floor(minutesLeft%1 * 60)) + " / "+(int)(minutes/60)+":" + (int) (minutes % 60)+":00");
        }
    }
    public void optionsShow(int index){
        options.show(index);
    }

    public void changeActivity(){
        ChangingActivity.change(this, "Home", "Menu", activities);
    }

    public void optionsHide(){
        options.hide();
    }

    public void chosenChanged(){
        circle.invalidate();
    }
    public void updateText(int minutes){
        hoursText.setText(Character.forDigit((int) Math.floor((float)minutes/60f), 24)+" hours\n" +(minutes % 60) + " min");
    }
    public int getChosen(){
        return colorMenu.chosen;
    }

    public void editEnd(){
        options.editEnd();
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

    public void cancelPress(View view){
        colorMenu.cancelPress(view);
    }

    public void confirmPress(View view){
        colorMenu.confirmPress(view);
    }

    public void playPress(View view){
        options.playPress();
    }

    public void deletePress(View view){
        options.deletePress();
    }

    public void editPress(View view){
        options.editPress();
    }

    public void addPress(View view){
        if (!options.isShown()) circle.addNew();
    }

    public void startPress(View view){
        playMenu.startPress(view);
    }

    public void listPress(View view){
        changeActivity();
    }
}
