package com.oleaf.eighthours;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.oleaf.eighthours.date.EightCalendar;
import com.oleaf.eighthours.details.DetailsFragment;
import com.oleaf.eighthours.settings.SavedSettings;
import com.oleaf.eighthours.settings.Settings;
import com.oleaf.eighthours.settings.SettingsActivity;

public class Home extends AppCompatActivity {
    public static final int grid = 20;
    public static final float button_bounds = 0.1f;

    public int index;

    public TextView hoursText, desc;
    public Activities activities;
    public Circle circle;
    public EightCalendar eightCalendar;
    public View textEditLayout;

    private TextEditor editText;
    private AddButton addButton;
    private ColorMenu colorMenu;
    private ImageButton close;
    private ColorPick colorPick;
    private Options options;
    private Animation showTwist, hideTwist;
    private DetailsFragment detailsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getExtras())
            activities = new Activities(getDefaultTime());

        setContentView(R.layout.activity_home);

        Resources r = getResources();



        hoursText = findViewById(R.id.textView);
        colorMenu = findViewById(R.id.color_menu);
        desc = findViewById(R.id.textView3);
        circle = findViewById(R.id.circle);
        options = findViewById(R.id.options);
        addButton = findViewById(R.id.addButton);
        colorPick = findViewById(R.id.menu_view);
        close = findViewById(R.id.closeButton);
        textEditLayout = findViewById(R.id.textEditLayout);
        editText = findViewById(R.id.edit_name);
        detailsFragment = DetailsFragment.newInstance();
        eightCalendar = new EightCalendar(activities, this, this);
        eightCalendar.readDate();
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
        updateTimeLeft();
        if (Build.VERSION.SDK_INT >= 27){
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.appWhite));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.appWhite));
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR |View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR   );
        }
    }

    public void updateDefaultTime(){
        activities.updateDefault(getDefaultTime());
    }

    public void changeActivities(Activities activities){
        this.activities = activities;
        activities.updateDefault(getDefaultTime());
        updateTimeLeft();
    }

    public void resetActivities(){
        activities = new Activities(getDefaultTime());
    }
    public void updateActivities(){
        circle.arcs = circle.new Arcs();
        for (Span s : activities.getSpans()){
            circle.arcs.addNew(circle.convertMinutes((int) s.minutes), s.color_index);
        }
        circle.invalidate();
    }
    public int addActivity(int min, int color){
        Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
        int ret = activities.newActivity(min, color, editText.getText().toString());
        editText.clearFocus();
        editText.setText("");
        return ret;//indicator.update(activities.getLength());
    }

    private void changeAddButton(AddButton.State state){
        //add_button.setText(state ? addText : confText);
        addButton.setState(state);
    }

    public void updateButton(boolean isPast){
        if (addButton.currentState == AddButton.State.ADDNEW && isPast){
            changeAddButton(AddButton.State.ADDNEW_INACTIVE);
        }else if (addButton.currentState == AddButton.State.ADDNEW_INACTIVE && !isPast){
            changeAddButton(AddButton.State.ADDNEW);
        }
    }

    public void editActivity(int index, int min, int color){
        activities.editActivity(index, min, (byte) color, editText.getText().toString());
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
            activities = new Activities(getDefaultTime());
        return true;
    }
    public boolean colorUp(){
        return colorMenu.isShown();
    }
    public void colorShow(){
        colorMenu.show();
        colorPick.noAnimation();
        desc.setText(R.string.drag_to_edit);
        changeAddButton(AddButton.State.CONFIRM);
    }
    public void colorShow(int color_index){
        colorMenu.show(color_index);
        colorPick.noAnimation();
        desc.setText(R.string.drag_to_edit);
        changeAddButton(AddButton.State.CONFIRM);
    }
    public void colorHide(){
        updateTimeLeft();
        desc.setText("");
        changeAddButton(AddButton.State.ADDNEW);
    }
    public void updateBottom(float minutesLeft, float minutes){
        if (options.index > -1){
            desc.setText(((int)(minutesLeft/60)+":"+(int)(minutesLeft%60)+":"+(int)Math.floor(minutesLeft%1 * 60)) + " / "+(int)(minutes/60)+":" + (int) (minutes % 60)+":00");
        }
    }
    public void optionsShow(int index){

    }

    public void fillEdit(int index){
        editText.setText(activities.getSpan(index).name);
    }

    public void changeActivity(Class<?> c, int req){
        Intent intent = new Intent(this, c);
        intent.putExtra("activities", (Parcelable) activities);
        startActivityForResult(intent, req);
    }

    public void optionsHide(){
        //add_button.setActivated(false);
        options.close();
    }

    public void updateTimeLeft(){
        updateText(activities.time_left);
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

    private void popUp(int index){
        detailsFragment.show(getSupportFragmentManager(), ""+activities.getSpan(index).color_index);
    }

    public void calendarPress(View view){
        eightCalendar.showDatePicker(getSupportFragmentManager());
    }

    public void deletePress(View view){
        options.deletePress();
    }

    public void editPress(View view){
        options.editPress();
    }

    public void addPress(View view){
        switch (addButton.currentState){
            case ADDNEW:
                circle.addNew();
                break;
            case CONFIRM:
                colorMenu.confirmPress();
                eightCalendar.saveActivities();
                break;
            case PLAY:
                closePress(null);
                popUp(0);
                break;
        }
    }

    public void closePress(View view){
        if (view != null)
            if (view.getVisibility() != View.VISIBLE)
                return;
        options.close();
        colorMenu.close();
        showHide();
        changeAddButton(AddButton.State.ADDNEW);
        updateButton(eightCalendar.isPast());
    }

    public void activitySelected(int index){
        this.index = index;
        options.show(index);
        addButton.currentState = eightCalendar.isToday() ? AddButton.State.PLAY : AddButton.State.PLAY_INACTIVE;
        addButton.setState(addButton.currentState);
    }

    public void showHide(){
        close.startAnimation(hideTwist);
        changeAddButton(AddButton.State.ADDNEW);
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

    public void onSettingsPressed(View view){
        changeActivity(SettingsActivity.class, 3);
    }

    private int getDefaultTime(){
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("default_time", ""+getResources().getInteger(R.integer.default_hours))) * 60;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 2:
                if (resultCode == Activity.RESULT_OK){
                    Activities a = data.getParcelableExtra("activities");
                    if (a != null){
                        activities = a;
                    }
                }
            case 3:
                //Settings
                if (resultCode == Activity.RESULT_OK){
                    SavedSettings settings = data.getParcelableExtra("settings");
                    updateDefaultTime();
                    updateTimeLeft();
                }
        }
    }
}
