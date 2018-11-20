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

    private ProgressLineView progressLineView;

    private static final String TAG = "ZoomLayout";
    private static final float MIN_ZOOM = 1f;
    private static final float MAX_ZOOM = 55.0f;

    public float getMax() {
        return max;
    }

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

    private void init(Context context) {
        threezoom = new ThreeHoursZoom();
        setWillNotDraw(false);
        paint.setColor(getResources().getColor(R.color.colorHorizontalFrontLine));
        paint.setStrokeWidth(33);
        paintTime.setColor(getResources().getColor(R.color.white));
        if (mSetScale != null) {
            mSetScale.setMaxScreenWidth(child().getWidth());
        }
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (mSetScale != null) {
                            mSetScale.setTouchX((int) motionEvent.getX());
                        }
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
                        if (mSetScale != null) {
                            lastClick = motionEvent.getX();
                            calculateClickedSecond();
                            mSetScale.secondClicked(clickedSecond);
                        }
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);

                if ((mode == Mode.DRAG && scale >= MIN_ZOOM) || mode == Mode.ZOOM) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = (child().getWidth() - (child().getWidth() / scale)) / 2 * scale;
                    max = maxDx;
                    float maxDy = (child().getHeight() - (child().getHeight() / scale)) / 2 * scale;
                    if (mSetScale != null) {
                        mSetScale.setMaxX((int) maxDx);
                    }
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
        float startVisibleSec = secsInDay * startVisiblePercent;
        float endVisibleSec = secsInDay * endVisiblePercent;
        threezoom.checkVisebility(startVisibleSec,endVisibleSec);
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

        void setTouchX(int x);

        void setMaxX(int maxX);

        void setMaxScreenWidth(int width);

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.colorHorizontalBackLine));
        canvas.drawRect(0, getHeight()/3, scaledViewWidth, getHeight()/3*2, paint);
        paintTime.setStrokeWidth((5/scale)<3?3:(5/scale));
//        canvas.drawLine(scaledViewWidth/8*2,0,scaledViewWidth/8*2,getHeight(),paintTime);
        if(visibleDevisers!=null) {
            for (int i = 0; i < visibleDevisers.size(); i++) {
                canvas.drawLine(visibleDevisers.get(i).getVisibleXPos(), 0, visibleDevisers.get(i).getVisibleXPos(), getHeight(), paintTime);
            }
        }

    }

    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        super.measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    private class Devider{
        boolean visible;
        int second;
        int visibleXPos;
        int mZoomLevel;

        public Devider(int pos,int zoomLevel){
            second = pos;
            mZoomLevel = zoomLevel;
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
            for(int i =0; i<8; i++){
                deviders.add(new Devider(SECS_IN_TIME/8*i,THREE_HOURS));
            }
            for(int i =0; i<24; i++){
                deviders.add(new Devider(SECS_IN_TIME/24*i,ONE_HOUR));
            }
            for(int i =0; i<48; i++){
                deviders.add(new Devider(SECS_IN_TIME/48*i,HALF_HOUR));
            }
            for(int i =0; i<96; i++){
                deviders.add(new Devider(SECS_IN_TIME/96*i,FIFTEEN_MINUTES));
            }
            for(int i =0; i<286; i++){
                deviders.add(new Devider(SECS_IN_TIME/286*i,FIVE_MINUTES));
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
}
