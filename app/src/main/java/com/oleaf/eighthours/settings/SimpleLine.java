package com.oleaf.eighthours.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.oleaf.eighthours.R;

public class SimpleLine extends View {
    private boolean dashed=false;
    public SimpleLine(Context context) {
        super(context);
        init(context, null);
    }

    public SimpleLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SimpleLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public SimpleLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    void init(Context c, AttributeSet attributeSet){
        if (attributeSet != null){
            TypedArray attrs = c.getTheme().obtainStyledAttributes(attributeSet, R.styleable.SimpleLine, 0, 0);
            try{
                dashed = attrs.getBoolean(R.styleable.SimpleLine_dashed, false);
            }finally {
                attrs.recycle();
            }
        }
        Toast.makeText(c, ""+dashed, Toast.LENGTH_SHORT).show();

    }
}
