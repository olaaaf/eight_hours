package com.oleaf.eighthours.menu;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

public class ActivityConstraint extends ConstraintLayout {
    public ActivityConstraint(Context context) {
        super(context);
        init(context);
    }

    public ActivityConstraint(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ActivityConstraint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context c){

    }
}
