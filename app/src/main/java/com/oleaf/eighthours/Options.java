package com.oleaf.eighthours;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class Options extends LinearLayout {
    Animation show, hide;
    int index=-1;

    public Options(Context context) {
        super(context);
        init(context);
    }

    public Options(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Options(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context c) {
        //Hide layout with also inflating children (setting to invisible or alpha=0 doesn't)
        setVisibility(VISIBLE);
        AlphaAnimation a = new AlphaAnimation(1, 0);
        a.setDuration(1);
        a.setFillAfter(true);
        startAnimation(a);

        //Load basic colorShow and animation
        show = AnimationUtils.loadAnimation(c, R.anim.menu_popup);
        hide = AnimationUtils.loadAnimation(c, R.anim.menu_down);
    }

    public void show(int index) {
        if (this.index > -1)
            this.index = index;
        else {
            ((Home) getContext()).showClose();
            this.index = index;
            bringToFront();
            startAnimation(show);
        }
    }

    public void hide() {
        this.index = -1;
        hideN();
        ((Home) getContext()).showHide();
    }

    private void hideN(){
        startAnimation(hide);
    }

    public void editPress() {
        if (index < 0)
            return;
        hideN();
        Home home = (Home) getContext();
        home.circle.edit();
        home.fillEdit(index);
    }

    public void editEnd(){
        index = -1;
    }

    public void deletePress() {
        if (index < 0)
            return;
        Home home = (Home) getContext();
        home.circle.arcs.deselect();
        home.circle.arcs.delete(index);
        home.circle.update();
        home.activities.deleteActivity(index);
        home.updateText(home.activities.time_left);
        hide();
    }

    public boolean isShown(){
        return (index > -1);
    }

    public void close(){
        if (index > -1) {
            hide();
            ((Home) getContext()).circle.arcs.deselect();
        }
    }
}
