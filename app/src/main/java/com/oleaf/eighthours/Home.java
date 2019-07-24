package com.oleaf.eighthours;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

public class Home extends AppCompatActivity {
    public static final int grid = 20;
    public static final float button_bounds = 0.1f;

    public TextView hoursText, desc, add_button;
    public Activities activities;
    public Circle circle;

    private String confText, addText;
    private ColorMenu colorMenu;
    private ImageButton close;
    private ColorPick colorPick;
    private Options options;
    private Animation showTwist, hideTwist;
    private boolean addState=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getExtras())
            activities = new Activities(this);

        setContentView(R.layout.activity_home);

        Resources r = getResources();
        confText = r.getString(R.string.confirm);
        addText = r.getString(R.string.addnew);

        hoursText = findViewById(R.id.textView);
        colorMenu = findViewById(R.id.color_menu);
        desc = findViewById(R.id.textView3);
        circle = findViewById(R.id.circle);
        options = findViewById(R.id.options);
        add_button = findViewById(R.id.addButton);
        colorPick = findViewById(R.id.menu_view);
        close = findViewById(R.id.closeButton);

        showTwist = AnimationUtils.loadAnimation(this, R.anim.show_twist);
        hideTwist = AnimationUtils.loadAnimation(this, R.anim.hide_twist);
        hideTwist.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                close.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (Build.VERSION.SDK_INT >= 27){
            getWindow().setNavigationBarColor(r.getColor(R.color.colorPrimary));
            getWindow().setStatusBarColor(r.getColor(R.color.colorPrimary));
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR |View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR   );
        }
    }

    @RequiresApi(api = 27)
    private void setNavBarColors(Resources r){

    }

    public int addActivity(int min, int color){
        return activities.newActivity(min, color);
        //indicator.update(activities.getLength());
    }

    /**
     * Change the text of add button
     * @param state: if true - add, else - confirm
     */
    private void changeAddButton(boolean state){
        add_button.setText(state ? addText : confText);
        addState = state;
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
    public boolean colorUp(){
        return colorMenu.isShown();
    }
    public void colorShow(){
        colorMenu.show();
        colorPick.noAnimation();
        desc.setText(R.string.drag_to_edit);
        changeAddButton(false);
    }
    public void colorShow(int color_index){
        colorMenu.show(color_index);
        colorPick.noAnimation();
        desc.setText(R.string.drag_to_edit);
        changeAddButton(false);
    }
    public void colorHide(){
        updateText(activities.time_left);
        desc.setText("");
        changeAddButton(true);
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
        Intent intent = new Intent(this, com.oleaf.eighthours.menu.Menu.class);
        intent.putExtra("activities", activities);
        startActivityForResult(intent, 2137);
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

    public void deletePress(View view){
        options.deletePress();
    }

    public void editPress(View view){
        options.editPress();
    }

    public void addPress(View view){
        if (addState){
            circle.addNew();
        }else{
            colorMenu.confirmPress();
        }
    }

    public void listPress(View view){
        changeActivity();
    }

    public void closePress(View view){
        options.close();
        colorMenu.close();
        showHide();
    }

    public void showHide(){
        close.startAnimation(hideTwist);
    }

    public void showClose(){
        close.setVisibility(View.VISIBLE);
        close.startAnimation(showTwist);
    }

    @Override
    public void onBackPressed() {
        if (close.getVisibility() == View.VISIBLE)
            closePress(null);
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2137 && resultCode == Activity.RESULT_OK){
            Activities a = data.getParcelableExtra("activities");
            if (a != null){
                activities = a;
            }
        }
    }
}
