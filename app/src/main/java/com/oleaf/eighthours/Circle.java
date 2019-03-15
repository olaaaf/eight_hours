package com.oleaf.eighthours;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Handler;
import android.util.AttributeSet;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class Circle extends View{
    public TypedArray colors;
    public static final float alpha_threshold = 45;
    private float alpha_pause;
    private float alpha_rounded;
    Paint paint;
    Resources resources;
    RectF rectangle;
    Home home;
    Vibrator vibrator;
    Gestures gestures;
    float start_alpha;
    boolean dragging, onRight, full;        //full - whether there's some time left
    Animation animation;
    Arcs arcs;
    Arcs.Arc dragArc;
    private Handler handlerUpdater;
    private Runnable runnable;
    private boolean handlerWorking;
    private static final int millisUpdate = 5;
    //TODO: delete
    public static final boolean rounded = true;
    public static final float touchRadius = 0.7f;

    public Circle(Context context){
        super(context);
        init(context);
    }
    public Circle(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(context);
    }
    public Circle(Context context, AttributeSet attributeSet, int defStyleAttr){
        super(context, attributeSet, defStyleAttr);
        init(context);
    }
    private void init(Context c){
        resources = c.getResources();
        home = (Home)getContext();
        handlerUpdater = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
                handlerUpdater.postDelayed(runnable, millisUpdate);
            }
        };
        paint = new Paint();
        paint.setAntiAlias(true);
        if (!rounded)
            paint.setStrokeWidth(resources.getDimension(R.dimen.circle_stroke));
        else
            paint.setStrokeWidth(resources.getDimension(R.dimen.circle_rounded));

        float width = resources.getDimension(R.dimen.circle_width)-paint.getStrokeWidth() / 2.0f;
        float height = resources.getDimension(R.dimen.circle_height)-paint.getStrokeWidth() / 2.0f;
        float w2 = resources.getDimension(R.dimen.circle_view_width) - resources.getDimension(R.dimen.circle_width);
        float h2 = resources.getDimension(R.dimen.circle_view_height) - resources.getDimension(R.dimen.circle_height);
        alpha_pause = resources.getDimension(R.dimen.alpha_break);
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        rectangle = new RectF(paint.getStrokeWidth() / 2.0f + w2/2.0f, paint.getStrokeWidth() / 2.0f + h2/2.0f, width+ w2/2.0f, height+h2/2.0f);
        colors = resources.obtainTypedArray(R.array.colors);
        animation = AnimationUtils.loadAnimation(c, R.anim.circle_up);
        gestures = new Gestures(64, -1) {
            @Override
            protected void onTap(float x, float y) {
                selectActivity( touchActivity(x, y), false);
                invalidate();
            }
            @Override
            protected void onDragStop(float x, float y) {
                if (!full && dragging){
                    calculateAlpha(x, y);
                    if (!home.isMenuUp())
                        menuUp();
                    invalidate();
                }
                selectActivity( touchActivity(x, y), false);
                //arcs.deselect();
            }
            @Override
            protected void onDrag(float x, float y) {
                if (!full && dragging && dragArc.animationFinished()){
                    dragArc.α = clamp(calculateAlpha(x, y) - (start_alpha+90), convertMinutes(Activities.grid), convertMinutes(home.activities.time_left));
                    home.updateText(convertAlpha(dragArc.α));
                    invalidate();
                } else if (home.activities.getSpans().length > 0 && touchActivity(x, y) > -1) {
                    selectActivity( touchActivity(x, y), true);
                    invalidate();
                }
            }
            @Override
            protected void longPress(float x, float y) {
                int tindex = touchActivity(x, y);
                if (tindex != -1){
                    arcs.select(tindex);
                }
            }
            @Override
            protected void longPressStop(float x, float y) {
                arcs.deselect();
            }
        };
        alpha_rounded = (float) Math.asin(paint.getStrokeWidth() / (height / 2f));
        arcs = new Arcs();
        dragArc = arcs.new Arc(0, resources.getColor(R.color.drag_color), false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //drawBaseCircle(canvas);
        start_alpha = arcs.drawAll(canvas);
        if (dragging){
            dragArc.drawRounded(start_alpha, canvas);
        }
        checkAnimations();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Home.viewContains(this, event.getX(), event.getY(), 0.2f ))
            gestures.touchEvent(event);
        return true;
    }
    private float calculateAlpha(float x, float y){
        float alpha = -1f * (float)(Math.toDegrees(Math.atan2(x-rectangle.width()/2.0f, y-rectangle.height()/2.0f))-180f);
        alpha = clampAlpha(alpha);
        return alpha;
    }
    private float calculateAlphaNC(float x, float y){
        return -1f * (float)(Math.toDegrees(Math.atan2(x-rectangle.width()/2.0f, y-rectangle.height()/2.0f))-180f);
    }
    private float calculateRadius(float x, float y){
        return (float) (Math.sqrt(x*x + y*y));
    }

    public int convertAlpha(float ... a)   {
        if (a.length > 0){
            return Math.round(Math.round(a[0]/360f * home.activities.maximum) / Home.grid) * Home.grid;
        }else{
            return Math.round(Math.round(dragArc.α/360f * home.activities.maximum) / Home.grid) * Home.grid;
        }
    }
    public float convertMinutes(int a){
        return ((float)a)/((float)home.activities.maximum)*360.0f;
    }
    private void drawBaseCircle(Canvas canvas){
        float stroke_width = paint.getStrokeWidth();
        paint.setStrokeWidth(resources.getDimension(R.dimen.circle_stroke));
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(resources.getColor(R.color.circle_color));
        canvas.drawArc(rectangle, 0, 360, true, paint);
        paint.setStrokeWidth(stroke_width);
    }

    private float clampAlpha(float ... alpha){
        return clampAlpha(false, alpha);
    }
    private float clampAlpha(boolean tap, float ... a){
        float alpha;
        if (a.length == 0)
            alpha = dragArc.α + start_alpha;
        else
            alpha = a[0];
        if (tap)
            return alpha;
        if (alpha >= (360 - alpha_threshold * 2) && alpha < (360-alpha_threshold)){
            onRight = false;
        }else if (alpha >= alpha_threshold  && alpha <= alpha_threshold * 2){
            onRight = true;
        }
        if (start_alpha + 90 > alpha_threshold){
            onRight = false;
        }
        if (alpha >= (360 - alpha_threshold) && onRight){
            alpha = 0;
        }else if(alpha < alpha_threshold && !onRight){
            alpha = 360;
        }
        if (alpha < start_alpha + 90){
            alpha = start_alpha + 90;
        }

        if (convertAlpha(alpha - (start_alpha + 90)) < Activities.grid) {
            alpha = convertMinutes(Activities.grid) + start_alpha + 90;
        }
        return alpha;
    }
    private int touchActivity(float x, float y){
        float a = calculateAlphaNC(x, y);
        float alphas[] = home.activities.getAlphas();
        float radius = (float) Math.sqrt(x*x + y*y);
        float a_start= 0;
        if (radius > touchRadius*(rectangle.width()/2.0f)){
            for (int ix = 0; ix < alphas.length; ++ix){
                if (a <= a_start + alphas[ix])
                    return ix;
                a_start += alphas[ix];
            }
        }
        return -1;
    }

    private void arcAnimation(){
        if (!handlerWorking){
            handlerUpdater.postDelayed(runnable, millisUpdate);
            handlerWorking = true;
        }
    }
    private void checkAnimations(){
        if (!handlerWorking)
            return;
        handlerWorking = false;
        if (handlerWorking = !dragArc.animationFinished())
            return;
        if (handlerWorking = !arcs.checkAnimations())
            return;
        if (!handlerWorking)
            handlerUpdater.removeCallbacks(runnable);
    }
    private boolean selectActivity(int index, boolean vibrate){
        if (index != -1 && !dragging){
            if (arcs.select(index)){
                if (vibrate) Vibrate.v(50,vibrator);
                //show Options
                return true;
            }
        }
        return false;
    }
    /*
    menu_layout functions
        TODO: deleting with the account of [full]
        TODO: add background circle
        TODO: direction detection (on the circle)
        TODO: fix selecting
     */
    public void menuUp(){
        if (convertAlpha() > 0)
            home.popup();
        else
            dragging = false;
    }
    public void cancel(){
        dragging = false;
        dragArc.α = 0;
        invalidate();
        home.updateText(home.activities.time_left);
    }
    public void confirm() {
        arcs.addNewAnimation(convertMinutes(convertAlpha(dragArc.α)), home.getChosen(), dragArc.α, home.addActivity(convertAlpha(dragArc.α), home.getChosen()));
        if (home.activities.time_left <= 0)
            full = true;
        dragging = false;
        dragArc.α = 0;
        invalidate();
    }
    public float getAngleBefore(int span_index){
        if (span_index < 1)
            return 0;
        float start = 0;
        Span[] sp = home.activities.getSpans();
        for (int ix = 0; ix <= (span_index - 1) && ix < sp.length; ++ix){
            start += convertMinutes((int) sp[ix].minutes);
        }
        return start;
    }
    public void addNew(){
        checkAnimations();
        if (home.activities.time_left <= 0)
            return;
        if (!dragging && !handlerWorking){
            startDragging();
            invalidate();
            menuUp();
            home.updateText(convertAlpha(dragArc.α));
        }
    }
    public static float clamp(float v, float min, float max){
        return ((v > max) ? max : ((v < min) ? min : v));
    }

    private void startDragging(){
        dragging = true; onRight = true;
        dragArc.α = convertMinutes((int) clamp(home.activities.time_left, Activities.grid,Activities.grid*3));
        dragArc.animate(Activities.grid);
    }

    private class ArcAnimation{
        private static final int default_time = 70;
        float time;
        long startTime;
        float addAlpha;
        float initialAlpha;

        ArcAnimation(float alpha){
            this(default_time, alpha, -1);
        }
        ArcAnimation(int milliseconds, float alpha){
            this(milliseconds, alpha, -1);
        }
        ArcAnimation(int milliseconds, float alpha, float additionalAlpha){
            time = (float) milliseconds;
            startTime = System.currentTimeMillis();
            initialAlpha = alpha;
            if (additionalAlpha > 0)
                addAlpha = additionalAlpha;
            else
                addAlpha = convertMinutes(Activities.grid);
        }
        ArcAnimation(float alpha, float additionalAlpha){
            this(default_time, alpha, additionalAlpha);
        }

        float getAlpha(){
            if (addAlpha < 0)
                return (getPart() * initialAlpha);
            return (getPart() * (initialAlpha - addAlpha) + addAlpha);
        }

        float getPart(){
            if (launched())
                return clamp((System.currentTimeMillis() - startTime) / time, 0f, 1f);
            else
                return 0;
        }
        boolean finished(){
            return (System.currentTimeMillis() - startTime >= time) & (launched());
        }

        //optional [without start]
        void start(float alpha){
            initialAlpha = alpha;
            start();
        }
        void start(){ startTime = System.currentTimeMillis(); }
        void stop(){ startTime = -1; }
        boolean launched(){ return (startTime > -1);}
    }

    //TODO: after play button show animation change the drawable

    public class Arcs{
        private  Arc[] arcs;
        int draggingIndex = -1;
        float shadowStroke;
        Arc shadow;

        Arcs(int shadow_color, float shadowStroke){
            arcs = new Arc[0];
            shadow = new Arc(0, shadow_color, false);
            this.shadowStroke = shadowStroke;
        }

        Arcs(){
            this(resources.getColor(R.color.arc_shadow), resources.getDimension(R.dimen.arc_sh_stroke));
        }


        public void addNew(float alpha, int color_index, int index){
            Arc[] cp = arcs.clone();
            arcs = new Arc[cp.length + 1];
            System.arraycopy(cp, 0, arcs, 0, cp.length);
            arcs[arcs.length - 1] = new Arc(alpha, color_index, index);
        }

        public void addNewAnimation(float alpha, int color_index, float alphaNow, int index){
            addNew(alpha, color_index, index);
            arcs[arcs.length - 1].animate(alphaNow);
        }

        public float drawAll(Canvas canvas){
            float start_alpha = -90;
            for (int ix = 0; ix < arcs.length; ++ix){
                if (ix == draggingIndex){
                    paint.setStrokeWidth(paint.getStrokeWidth() + shadowStroke);
                    shadow.color = arcs[ix].color;
                    shadow.drawRounded(start_alpha, canvas);
                    paint.setStrokeWidth(paint.getStrokeWidth() - shadowStroke);
                }
                start_alpha += arcs[ix].drawRounded(start_alpha, canvas);
            }
            return start_alpha;
        }
        //animation
        public boolean delete(int activityIndex){
            int find = findIndex(activityIndex);
            if (find > -1) {
                arcs = (Arc[]) Tools.removeIndex(arcs, find);
                return true;
            }
            return false;
        }

        public int findIndex(int activityIndex){
            for (int i = 0; i < arcs.length; ++i){
                if (arcs[i].index == activityIndex)
                    return i;
            }
            return -1;
        }

        public boolean checkAnimations(){
            for (Arc arc : arcs){
                if (!arc.animation.finished())
                    return false;
            }
            return true;
        }

        public boolean select(int index){
            boolean r = (draggingIndex < 0) ? false : (draggingIndex !=index);
            draggingIndex = (int) clamp(index, 0, arcs.length-1);
            shadow.α = arcs[index].α;
            return r;
        }

        public void deselect(){
            draggingIndex = -1;
        }

        public class Arc{
            int color;
            float α;
            int index;
            ArcAnimation animation;

            Arc(float α, int color_index){
                this.α = α;
                this.color = colors.getColor(color_index, 0);
            }

            Arc(float α, int color, boolean t){
                this.α = α;
                this.color = color;
            }

            Arc(float α, int color_index, int index){
                this.α = α;
                color = colors.getColor(color_index, 0);
                this.index = index;
            }

            public void animate( float fromAlpha){
                animation = new ArcAnimation(ArcAnimation.default_time, α, fromAlpha);
                arcAnimation();
            }

            public void stop(){
                animation.stop();
            }

            public boolean animationFinished(){ return animation.finished(); }

            public float drawNormal(float startAlpha, Canvas canvas){
                paint.setColor(color);
                float a = (animation != null) ? ((animation.launched()) ? animation.getAlpha() : α) : α;
                canvas.drawArc(rectangle, startAlpha, a, false, paint);
                return a;
            }

            public float drawRounded(float startAlpha, Canvas canvas){
                paint.setColor(color);
                float a = (animation != null) ? ((animation.launched() && !animation.finished()) ? animation.getAlpha() : α) : α;
                float startAngle = alpha_pause + startAlpha;
                float sweepAngle = a - alpha_pause * 2;
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(rectangle.width()/2f * (float) Math.cos(Math.toRadians(startAngle+alpha_rounded)) + rectangle.left + rectangle.width()/2.0f, rectangle.height()/2f + rectangle.top + rectangle.width()/2f * (float) Math.sin(Math.toRadians(startAngle+alpha_rounded)), paint.getStrokeWidth() / 2f, paint);
                canvas.drawCircle(rectangle.width()/2f * (float) Math.cos(Math.toRadians(sweepAngle-alpha_rounded+startAngle)) + rectangle.left + rectangle.width()/2.0f, rectangle.height()/2.0f + rectangle.top + rectangle.width()/2f * (float) Math.sin(Math.toRadians(sweepAngle-alpha_rounded+startAngle)), paint.getStrokeWidth() / 2f, paint);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(rectangle, startAngle+alpha_rounded, sweepAngle-alpha_rounded, false, paint);
                return a;
            }

            private void roundedMoved(float startAlpha, Canvas canvas, float x, float y){
                rectangle.top += y; rectangle.bottom += y;
                rectangle.left += x; rectangle.right += x;
                drawRounded(startAlpha, canvas);
                rectangle.top -= y; rectangle.bottom -= y;
                rectangle.left -= x; rectangle.right -= x;
            }
        }
    }
}