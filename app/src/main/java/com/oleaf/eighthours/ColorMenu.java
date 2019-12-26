package com.oleaf.eighthours;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class ColorMenu extends ConstraintLayout {
    public Animation showA, hideA;
    private boolean shown;
    TypedArray colors;
    int chosen;

    public ColorMenu(Context context) {
        super(context);
    }

    public ColorMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context c) {
        //Load resources
        Resources resources = c.getResources();
        showA = AnimationUtils.loadAnimation(c, R.anim.menu_popup);
        hideA = AnimationUtils.loadAnimation(c, R.anim.menu_down);
        colors = resources.obtainTypedArray(R.array.colors);
        shown = false;
        setVisibility(VISIBLE);
        setAlpha(1);
        //To colorHide the layout effectively, set alpha to 1 and colorHide it with an animation
        Animation a = new AlphaAnimation(1, 0);
        a.setFillAfter(true);
        a.setDuration(1);
        startAnimation(a);

        //when an animation ends, the parameters stay as it was still going
    }

    @Override
    protected void onFinishInflate() {
        init(getContext());
        super.onFinishInflate();
    }

    public void show(){
        show ((int) Math.round(Math.random() * (colors.length()-1)));
    }

    public void show(int index){
        setVisibility(VISIBLE);
        bringToFront();
        chosen = index;
        shown = true;
        //Change visibility
        //Play show animation with alpha
        startAnimation(showA);
        //zero out the buttons
        Home home = (Home) getContext();
        home.showClose();
        home.textEditLayout.setVisibility(VISIBLE);
    }

    public int hide(){
        shown = false;
        //Play colorHide animation
        startAnimation(hideA);
        ((Home) getContext()).colorHide();
        ((Home) getContext()).showHide();
        ((Home) getContext()).textEditLayout.setVisibility(GONE);
        return chosen;
    }

    public void confirmPress(){
        if (!shown)
            return;
        ((Home) getContext()).circle.confirm();
        hide();
    }

    public boolean isShown(){
        return shown;
    }

    public void close(){
        if (!shown)
            return;
        ((Home) getContext()).circle.cancel();
        hide();
        chosen = -1;
    }

}
