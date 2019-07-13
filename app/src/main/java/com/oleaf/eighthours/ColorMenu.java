package com.oleaf.eighthours;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ColorMenu extends ConstraintLayout {
    private TextView confirm, cancel;
    private Animation showA, hideA;
    private boolean shown;
    private int normalColor, inactiveColor;
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
        normalColor = resources.getColor(R.color.primary_text);
        inactiveColor = resources.getColor(R.color.inactive_text);
        //Get children
        for (int ix = 0; ix < getChildCount(); ++ix) {
            View getChild = getChildAt(ix);
            switch (getChild.getId()) {
                case R.id.menu_view:
                    break;
                case R.id.confirm:
                    confirm = (TextView) getChild;
                    break;
                case R.id.cancel:
                    cancel = (TextView) getChild;
                    break;
            }
        }
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
        chosen = index;
        shown = true;
        //Change visibility
        //Play show animation with alpha
        startAnimation(showA);
        //zero out the buttons
        cancel.setText(R.string.cancel);
        confirm.setText(R.string.confirm);
        ((Home) getContext()).showClose();
    }

    public int hide(){
        shown = false;
        //Play colorHide animation
        startAnimation(hideA);
        ((Home) getContext()).colorHide();
        ((Home) getContext()).showHide();
        return chosen;
    }

    public void confirmPress(View view){
        if (!shown)
            return;
        ((Home) getContext()).circle.confirm();
        hide();
    }

    public void cancelPress(View view){
        if (!shown)
            return;
        ((Home) getContext()).circle.cancel();
        hide();
        chosen = -1;
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
