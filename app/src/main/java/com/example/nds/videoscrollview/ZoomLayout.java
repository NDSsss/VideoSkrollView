package com.example.nds.videoscrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Layout that provides pinch-zooming of content. This view should have exactly one child
 * view containing the content.
 */
public class ZoomLayout extends FrameLayout implements ScaleGestureDetector.OnScaleGestureListener {
    /**
     * Max allowed duration for a "click", in milliseconds.
     */
    private static final int MAX_CLICK_DURATION = 250;

    /**
     * Max allowed distance to move during a "click", in DP.
     */
    private static final int MAX_CLICK_DISTANCE = 15;
    private static final String[] THRE_HOURS_VALUES = {"00:00","03:00","06:00","09:00","12:00","15:00","18:00","21:00","27:00"};
    private static final String[] ONE_HOUR_VALUES = {"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00",
                                                    "10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00","24:00"};
    private static final String[] HALF_HOUR_VALUES = {
            "00:00","00:30",
            "01:00","01:30",
            "02:00","02:30",
            "03:00","03:30",
            "04:00","04:30",
            "05:00","05:30",
            "06:00","06:30",
            "07:00","07:30",
            "08:00","08:30",
            "09:00","09:30",
            "10:00","10:30",
            "11:00","11:30",
            "12:00","12:30",
            "13:00","13:30",
            "14:00","14:30",
            "15:00","15:30",
            "16:00","16:30",
            "17:00","17:30",
            "18:00","18:30",
            "19:00","19:30",
            "20:00","20:30",
            "21:00","21:30",
            "22:00","22:30",
            "23:00","23:30",
            "24:00" };
    private static final String[] FIFTEEN_MINUTES_VALUES = {
            "00:00","00:15","00:30","00:45",
            "01:00","01:15","01:30","01:45",
            "02:00","02:15","02:30","02:45",
            "03:00","03:15","03:30","03:45",
            "04:00","04:15","04:30","04:45",
            "05:00","05:15","05:30","05:45",
            "06:00","06:15","06:30","06:45",
            "07:00","07:15","07:30","07:45",
            "08:00","08:15","08:30","08:45",
            "09:00","09:15","09:30","09:45",
            "10:00","10:15","10:30","10:45",
            "11:00","11:15","11:30","11:45",
            "12:00","12:15","12:30","12:45",
            "13:00","13:15","13:30","13:45",
            "14:00","14:15","14:30","14:45",
            "15:00","15:15","15:30","15:45",
            "16:00","16:15","16:30","16:45",
            "17:00","17:15","17:30","17:45",
            "18:00","18:15","18:30","18:45",
            "19:00","19:15","19:30","19:45",
            "20:00","20:15","20:30","20:45",
            "21:00","21:15","21:30","21:45",
            "22:00","22:15","22:30","22:45",
            "23:00","23:15","23:30","23:45",
            "24:00"};
    private static final String[] FIVE_MINUTES_VALUES = {
            "00:00","00:05","00:10","00:15","00:20","00:25","00:30","00:35","00:40","00:45","00:50","00:55",
            "01:00","01:05","01:10","01:15","01:20","01:25","01:30","01:35","01:40","01:45","01:50","01:55",
            "02:00","02:05","02:10","02:15","02:20","02:25","02:30","02:35","02:40","02:45","02:50","02:55",
            "03:00","03:05","03:10","03:15","03:20","03:25","03:30","03:35","03:40","03:45","03:50","03:55",
            "04:00","04:05","04:10","04:15","04:20","04:25","04:30","04:35","04:40","04:45","04:50","04:55",
            "05:00","05:05","05:10","05:15","05:20","05:25","05:30","05:35","05:40","05:45","05:50","05:55",
            "06:00","06:05","06:10","06:15","06:20","06:25","06:30","06:35","06:40","06:45","06:50","06:55",
            "07:00","07:05","07:10","07:15","07:20","07:25","07:30","07:35","07:40","07:45","07:50","07:55",
            "08:00","08:05","08:10","08:15","08:20","08:25","08:30","08:35","08:40","08:45","08:50","08:55",
            "09:00","09:05","09:10","09:15","09:20","09:25","09:30","09:35","09:40","09:45","09:50","09:55",
            "10:00","10:05","10:10","10:15","10:20","10:25","10:30","10:35","10:40","10:45","10:50","10:55",
            "11:00","11:05","11:10","11:15","11:20","11:25","11:30","11:35","11:40","11:45","11:50","11:55",
            "12:00","12:05","12:10","12:15","12:20","12:25","12:30","12:35","12:40","12:45","12:50","12:55",
            "13:00","13:05","13:10","13:15","13:20","13:25","13:30","13:35","13:40","13:45","13:50","13:55",
            "14:00","14:05","14:10","14:15","14:20","14:25","14:30","14:35","14:40","14:45","14:50","14:55",
            "15:00","15:05","15:10","15:15","15:20","15:25","15:30","15:35","15:40","15:45","15:50","15:55",
            "16:00","16:05","16:10","16:15","16:20","16:25","16:30","16:35","16:40","16:45","16:50","16:55",
            "17:00","17:05","17:10","17:15","17:20","17:25","17:30","17:35","17:40","17:45","17:50","17:55",
            "18:00","18:05","18:10","18:15","18:20","18:25","18:30","18:35","18:40","18:45","18:50","18:55",
            "19:00","19:05","19:10","19:15","19:20","19:25","19:30","19:35","19:40","19:45","19:50","19:55",
            "20:00","20:05","20:10","20:15","20:20","20:25","20:30","20:35","20:40","20:45","20:50","20:55",
            "21:00","21:05","21:10","21:15","21:20","21:25","21:30","21:35","21:40","21:45","21:50","21:55",
            "22:00","22:05","22:10","22:15","22:20","22:25","22:30","22:35","22:40","22:45","22:50","22:55",
            "23:00","23:05","23:10","23:15","23:20","23:25","23:30","23:35","23:40","23:45","23:50","23:55",
            "24:00"};
    public final static int SECS_IN_TIME = 86400;
    public static final int THREE_HOURS = 1;
    public static final int ONE_HOUR = 2;
    public static final int HALF_HOUR = 3;
    public static final int FIFTEEN_MINUTES = 4;
    public static final int FIVE_MINUTES = 5;
    public static final int THREE_HOURS_LEVEL = 86400;
    public static final int ONE_HOUR_LEVEL = 57600;
    public static final int HALF_HOUR_LEVEL = 25200;
    public static final int FIFTEEN_MINUTES_LEVEL = 10800;
    public static final int FIVE_MINUTES_LEVEL = 4800;
    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    private static final String TAG = "ZoomLayout";
    private static final float MIN_ZOOM = 1f;
    private static final float MAX_ZOOM = 55.0f;
    float max;
    private float secsInDay = 86400;
    private Mode mode = Mode.NONE;
    private float scale = 1.0f;
    private float lastScaleFactor = 0f;
    private IZoomCallback mSetScale;
    private boolean getScreenWidth = false;
    private float screenWidth = 0;
    private float screetHeight = 0;
    private float scaledViewWidth = 0;
    private float lastClick = 1;
    private float clickedSecond = 1;
    private int[] location = new int[2];
    private ThreeHoursZoom threezoom;
    private ArrayList<Devider> visibleDevisers;
    private long pressStartTime;
    private float pressedX;
    private float pressedY;
    private int currentProgress=0;
    float endVisibleSec=SECS_IN_TIME;
    float startVisibleSec = 0f;
    private ProgressCursor progressCursor;

    // Where the finger first  touches the screen
    private float startX = 0f;
    private float startY = 0f;

    // How much to translate the canvas
    private float dx = 0f;
    private float dy = 0f;

    private float prevDx = 0f;
    private float prevDy = 0f;

    Paint paint = new Paint();
    Paint paintTime = new Paint();
    Paint paintProgress = new Paint();

    public ZoomLayout(Context context) {
        super(context);
        init(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setISetScaleListener(IZoomCallback setScaleListener) {
        mSetScale = setScaleListener;
    }

    public void setProgress(int progress){
        currentProgress = progress;
        progressCursor.init(progress);

    }

    private void init(Context context) {
        threezoom = new ThreeHoursZoom();
        setWillNotDraw(false);
        paint.setColor(getResources().getColor(R.color.colorHorizontalFrontLine));
        paint.setStrokeWidth(33);
        paintTime.setColor(getResources().getColor(R.color.white));
        paintTime.setTextAlign(Paint.Align.CENTER);
        paintTime.setTextSize(30);
        paintProgress.setColor(getResources().getColor(R.color.colorAccent));
        threezoom.checkVisebility(0,SECS_IN_TIME);
        visibleDevisers = threezoom.getVisibleDeviders(getZoomLevel(0,SECS_IN_TIME));
        progressCursor = new ProgressCursor();
        progressCursor.init(SECS_IN_TIME/2);
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        pressStartTime = System.currentTimeMillis();
                        pressedX = motionEvent.getX();
                        pressedY = motionEvent.getY();
                        Log.i(TAG, "DOWN");
                        if (scale > MIN_ZOOM) {
                            mode = Mode.DRAG;
                            startX = motionEvent.getX() - prevDx;
                            lastClick = motionEvent.getX();
                            startY = motionEvent.getY() - prevDy;
                            Log.d(TAG, "DOWN TO SCALE startX " + startX + " motionEvent.getX() " + motionEvent.getX() + " prevDx " + prevDx);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == Mode.DRAG) {
                            dx = motionEvent.getX() - startX;
                            dy = motionEvent.getY() - startY;
                            Log.d(TAG, "MOVE stratx " + String.valueOf(startX) + " dx " + String.valueOf(dx) + " motion x " + String.valueOf(motionEvent.getX()));
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mode = Mode.ZOOM;
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = Mode.DRAG;
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "UP");
                        mode = Mode.NONE;
                        prevDx = dx;
                        prevDy = dy;
                        long pressDuration = System.currentTimeMillis() - pressStartTime;
                        if (pressDuration < MAX_CLICK_DURATION && distance(pressedX, pressedY, motionEvent.getX(), motionEvent.getY()) < MAX_CLICK_DISTANCE) {
                            if (mSetScale != null) {
                                lastClick = motionEvent.getX();
                                calculateClickedSecond();
                                mSetScale.secondClicked(clickedSecond);
                            }
                        }
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);

                if ((mode == Mode.DRAG && scale >= MIN_ZOOM) || mode == Mode.ZOOM) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = (child().getWidth() - (child().getWidth() / scale)) / 2 * scale;
                    max = maxDx;
                    float maxDy = (child().getHeight() - (child().getHeight() / scale)) / 2 * scale;
                    dx = Math.min(Math.max(dx, -maxDx), maxDx);
                    dy = Math.min(Math.max(dy, -maxDy), maxDy);
//                    Log.i(TAG, "Width: " + child().getWidth() + ", scale " + scale + ", dx " + dx
//                            + ", max " + maxDx);
                    applyScaleAndTranslation();
                }

                return true;
            }
        });
    }
    private float distance(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
        return pxToDp(distanceInPx);
    }

    private float pxToDp(float px) {
        return px / getResources().getDisplayMetrics().density;
    }

    // ScaleGestureDetector

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleDetector) {
        Log.i(TAG, "onScaleBegin");
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleDetector) {
        float scaleFactor = scaleDetector.getScaleFactor();
        Log.i(TAG, "onScale" + scaleFactor);
        if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
            scale *= scaleFactor;
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
            lastScaleFactor = scaleFactor;
            scaledViewWidth = (int) (screenWidth * scale);
            if (mSetScale != null) {
                mSetScale.setScale(scale);
            }
        } else {
            lastScaleFactor = 0;
        }
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleDetector) {
        Log.i(TAG, "onScaleEnd");
    }

    private void applyScaleAndTranslation() {
        child().setScaleX(scale);
//        child().setScaleY(scale);
        child().setTranslationX(dx);
        child().getLocationOnScreen(location);
        Log.d(TAG, "child().getLocationOnScreen(location) " + String.valueOf(location[0]) + " " + String.valueOf(location[1]));
        Log.d(TAG, "scaledViewWidth: " + scaledViewWidth);
        Log.d(TAG, "screenWidth: " + screenWidth);
        calculateClickedSecond();
        invalidate();
    }

    void calculateClickedSecond() {
        float stastVisibleX = -location[0] + 1;
        float endVisibleX = screenWidth + stastVisibleX - 1;
        float startVisiblePercent = stastVisibleX / scaledViewWidth;
        float endVisiblePercent = endVisibleX / scaledViewWidth;
        Log.d(TAG, "stastVisibleX: " + stastVisibleX + " endVisibleX " + endVisibleX);
        Log.d(TAG, "startVisiblePercent: " + startVisiblePercent + " endVisiblePercent " + endVisiblePercent);
        startVisibleSec = secsInDay * startVisiblePercent;
        endVisibleSec = secsInDay * endVisiblePercent;
        threezoom.checkVisebility(startVisibleSec,endVisibleSec);
        progressCursor.init(currentProgress);
        visibleDevisers = threezoom.getVisibleDeviders(getZoomLevel(startVisibleSec,endVisibleSec));
        Log.d(TAG, "startVisibleSec: " + startVisibleSec + " endVisibleSec " + endVisibleSec);
        float visibleSecs = endVisibleSec - (int) startVisibleSec;
        Log.d(TAG, "visibleSecs: " + visibleSecs);
        clickedSecond = startVisibleSec + (visibleSecs * (lastClick / screenWidth));
        Log.d(TAG, "clickedSecond: " + clickedSecond);
    }

    private int getZoomLevel(float startSec, float endSec){
        float visibleSeconds = endSec - startSec;
        if(visibleSeconds<=THREE_HOURS_LEVEL&&visibleSeconds>ONE_HOUR_LEVEL){
            return THREE_HOURS;
        } else if(visibleSeconds<=ONE_HOUR_LEVEL&&visibleSeconds>HALF_HOUR_LEVEL){
            return ONE_HOUR;
        } else if(visibleSeconds<=HALF_HOUR_LEVEL&&visibleSeconds>FIFTEEN_MINUTES_LEVEL){
            return HALF_HOUR;
        }else if(visibleSeconds<=FIFTEEN_MINUTES_LEVEL&&visibleSeconds>FIVE_MINUTES_LEVEL){
            return FIFTEEN_MINUTES;
        }else if(visibleSeconds<=FIVE_MINUTES_LEVEL){
            return FIVE_MINUTES;
        }

        return 0;
    }

    private View child() {
        return getChildAt(0);
    }

    public interface IZoomCallback {
        void setScale(float scale);
        void secondClicked(float clickedSecond);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        screenWidth = getWidth();
        screetHeight = getHeight();
        scaledViewWidth = screenWidth * scale;
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.colorHorizontalBackLine));
        canvas.drawRect(0, getHeight()/3, scaledViewWidth, getHeight()/3*2, paint);
        paintTime.setStrokeWidth((5/scale)<3?3:(5/scale));
        paintProgress.setStrokeWidth((10/scale)<7?7:(10/scale));
//        canvas.drawLine(scaledViewWidth/8*2,0,scaledViewWidth/8*2,getHeight(),paintTime);
        if(visibleDevisers!=null) {
            for (int i = 0; i < visibleDevisers.size(); i++) {
                canvas.drawLine(visibleDevisers.get(i).getVisibleXPos(), getHeight()/3, visibleDevisers.get(i).getVisibleXPos(), getHeight(), paintTime);
                canvas.drawText(visibleDevisers.get(i).getTime(),visibleDevisers.get(i).getVisibleXPos(),getHeight()/3,paintTime);
            }
        }

        if(progressCursor.isVisible()){
            canvas.drawLine(progressCursor.getPosX(),0,progressCursor.getPosX(),getHeight(),paintProgress);
        }


    }

    private class Devider{
        boolean visible;
        int second;
        int visibleXPos;
        int mZoomLevel;
        String time;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Devider(int pos, int zoomLevel, String mTime){
            second = pos;
            mZoomLevel = zoomLevel;
            time = mTime;
        }

        public int getZoomLevel() {
            return mZoomLevel;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public int getSecond() {
            return second;
        }

        public int getVisibleXPos() {
            return visibleXPos;
        }

        public void setVisibleXPos(int visibleXPos) {
            this.visibleXPos = visibleXPos;
        }
    }

    private class ThreeHoursZoom{
        ArrayList<Devider> deviders;

        public ThreeHoursZoom(){
            deviders = new ArrayList<>();
            for(int i =0; i<THRE_HOURS_VALUES.length; i++){
                deviders.add(new Devider(SECS_IN_TIME/THRE_HOURS_VALUES.length*i,THREE_HOURS,THRE_HOURS_VALUES[i]));
            }
            for(int i =0; i<ONE_HOUR_VALUES.length; i++){
                deviders.add(new Devider(SECS_IN_TIME/ONE_HOUR_VALUES.length*i,ONE_HOUR,ONE_HOUR_VALUES[i]));
            }
            for(int i =0; i<HALF_HOUR_VALUES.length; i++){
                deviders.add(new Devider(SECS_IN_TIME/HALF_HOUR_VALUES.length*i,HALF_HOUR,HALF_HOUR_VALUES[i]));
            }
            for(int i =0; i<FIFTEEN_MINUTES_VALUES.length; i++){
                deviders.add(new Devider(SECS_IN_TIME/FIFTEEN_MINUTES_VALUES.length*i,FIFTEEN_MINUTES,FIFTEEN_MINUTES_VALUES[i]));
            }
            for(int i =0; i<FIVE_MINUTES_VALUES.length; i++){
                deviders.add(new Devider(SECS_IN_TIME/FIVE_MINUTES_VALUES.length*i,FIVE_MINUTES,FIVE_MINUTES_VALUES[i]));
            }
        }

        public void checkVisebility(float startSec, float endSec){
            for(int i=0; i < deviders.size();i++){
                if(deviders.get(i).getSecond()>=startSec&&deviders.get(i).getSecond()<=endSec){
                    deviders.get(i).setVisible(true);
                    deviders.get(i).setVisibleXPos((int)(screenWidth*(deviders.get(i).getSecond() - startSec)/(endSec-startSec)));
                } else {
                    deviders.get(i).setVisible(false);
                }
            }
        }

        public ArrayList<Devider> getVisibleDeviders(int zoomLevel){
            ArrayList<Devider> result = new ArrayList<>();
            for(int i = 0; i< deviders.size();i++){
                if(deviders.get(i).isVisible()&&deviders.get(i).getZoomLevel()==zoomLevel){
                    result.add(deviders.get(i));
                }
            }
            return result;
        }

    }

    private class ProgressCursor{
        private boolean isVisible;
        private float posX;
        public void init(float progress){
            if(progress>=startVisibleSec&&progress<=endVisibleSec){
                isVisible = true;
                posX = (screenWidth*(progress - startVisibleSec)/(endVisibleSec-startVisibleSec));
            } else {
                isVisible = false;
            }
        }

        public boolean isVisible() {
            return isVisible;
        }

        public void setVisible(boolean visible) {
            isVisible = visible;
        }

        public float getPosX() {
            return posX;
        }

        public void setPosX(float posX) {
            this.posX = posX;
        }
    }
}
