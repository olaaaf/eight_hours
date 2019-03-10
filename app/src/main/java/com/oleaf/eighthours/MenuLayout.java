package com.oleaf.eighthours;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class MenuLayout extends ConstraintLayout {
    private TextView confirm, cancel;
    TypedArray colors;
    private Resources resources;
    int chosen;
    Menu menu;

    public MenuLayout(Context context) {
        super(context);
    }

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context c) {
        resources = c.getResources();
        colors = resources.obtainTypedArray(R.array.colors);
        for (int ix = 0; ix < getChildCount(); ++ix) {
            View getChild = getChildAt(ix);
            switch (getChild.getId()) {
                case R.id.menu_view:
                    break;
                case R.id.confirm:
                    cancel = (TextView) getChild;
                    break;
                case R.id.cancel:
                    confirm = (TextView) getChild;
                    break;
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        init(getContext());
        super.onFinishInflate();
    }


}
