package com.oleaf.eighthours.details;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.oleaf.eighthours.Home;
import com.oleaf.eighthours.R;
import com.oleaf.eighthours.Span;

public class DetailsFragment extends BottomSheetDialogFragment {
    public static final float minPart=0.01f, maxPart=0.02f;
    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }
    public TypedArray colors;
    private ActivityUpdater activityUpdater;
    private Drawable pause, start;
    private ImageButton playButton;
    private float minSkip=0.5f, maxSkip=0.5f;
    private float shortSkip=0.5f;

    Runnable changeDrawable = new Runnable() {
        @Override
        public void run() {
            if (activityUpdater.isRunning()){
                playButton.setImageDrawable(start);
            }else{
                playButton.setImageDrawable(pause);
            }
        }
    };

    @Override
    public int getTheme() {
        return R.style.SheetDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog d =  new BottomSheetDialog(requireContext(), getTheme());
        Resources resources = d.getContext().getResources();
        colors = resources.obtainTypedArray(R.array.colors);

        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout frameLayout = d.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(frameLayout).setPeekHeight((int) getContext().getResources().getDimension(R.dimen.peek_height));
                BottomSheetBehavior.from(frameLayout).setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setWhiteNavigationBar(d);
        }
        return d;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //get chosen span
        Home home = (Home) getContext();
        Span span = home.activities.getSpan(home.index);
        //inflate the view
        View v = inflater.inflate(R.layout.activity_details, container, false);
        //load resources
        start = ContextCompat.getDrawable(getContext(), R.drawable.play_na);
        pause = ContextCompat.getDrawable(getContext(), R.drawable.stop_na);
        //make layout changes
        //get all the views
        TextView activityName = v.findViewById(R.id.activity_name);
        TextView left = v.findViewById(R.id.time_left);
        ProgressBar progressBar = v.findViewById(R.id.progress);
        activityUpdater = v.findViewById(R.id.updater);
        playButton = v.findViewById(R.id.playButton);
        ImageView forward = v.findViewById(R.id.right_arrow);
        ImageView backward = v.findViewById(R.id.left_arrow);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        forward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });
        forward.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                forward();
                return true;
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shortForward();
            }
        });
        backward.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                backward();
                return true;
            }
        });
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shortBackward();
            }
        });
        View.OnTouchListener t = new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent){
                int x = motionEvent.getAction();
                if (x == MotionEvent.ACTION_UP || x == MotionEvent.ACTION_OUTSIDE ){
                    //To allow easier forwarding/backing the timer has to be updated more frequently
                    //The following command resets the update interval to default (after finger moved up)
                    activityUpdater.stopSkipping();
                }
                return false;
            }
        };
        backward.setOnTouchListener(t);
        forward.setOnTouchListener(t);
        //add views to the updater and then initialize it (on 0)
        activityUpdater.init(span, progressBar, left, changeDrawable);
        activityUpdater.update();
        //update name
        activityName.setText((span.getName().equals(Span.default_name)) ? span.getName()+ " " + (home.index+1) : span.getName());
        activityName.setTextColor(colors.getColor(span.getColorIndex(), 0xFFFF00FF));
        //set progress bar color
        progressBar.setColor(colors.getColor(span.getColorIndex(), 0xFFFF00FF));
        if (span.isOnGoing())
            play();
        //change skip values
        minSkip = minPart * span.getMinutes();
        maxSkip = maxPart * span.getMinutes();
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setWhiteNavigationBar(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(ContextCompat.getColor(dialog.getContext(), R.color.colorPrimary));

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }

    public void play(){
        changeDrawable();
        if (activityUpdater.isRunning()){
            activityUpdater.stop();
        }else{
            activityUpdater.start();
        }
    }

    private void changeDrawable(){
        changeDrawable.run();
    }

    public void forward(){
        activityUpdater.startSkipping(minSkip, maxSkip);
    }

    public void backward() {
        activityUpdater.startSkipping(-minSkip, -maxSkip);
    }

    public void shortForward(){
        activityUpdater.span.addActiveMinutes(shortSkip);
    }

    public void shortBackward(){
        activityUpdater.span.addActiveMinutes(-shortSkip);
    }
}
