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

/**
 * Layout that provides pinch-zooming of content. This view should have exactly one child
 * view containing the content.
 */
public class ZoomLayout extends FrameLayout implements ScaleGestureDetector.OnScaleGestureListener {

    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    private ProgressLineView progressLineView;

    private static final String TAG = "ZoomLayout";
    private static final float MIN_ZOOM = 0.10f;
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
    private float screenWidth=0;
    private float scaledViewWidth = 0;
    private float lastClick=1;
    private float clickedSecond =1;
    private int[] location = new int[2];

    // Where the finger first  touches the screen
    private float startX = 0f;
    private float startY = 0f;

    // How much to translate the canvas
    private float dx = 0f;
    private float dy = 0f;

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public float getPrevDx() {
        return prevDx;
    }

    public float getPrevDy() {
        return prevDy;
    }

    private float prevDx = 0f;
    private float prevDy = 0f;

    Paint paint = new Paint();
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

    public void setISetScaleListener(IZoomCallback setScaleListener){
        mSetScale =setScaleListener;
    }

    private void init(Context context) {
        paint.setStrokeWidth(10);
        if(mSetScale!=null){
            mSetScale.setMaxScreenWidth(child().getWidth());
        }
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if(mSetScale!=null){
                            mSetScale.setTouchX((int)motionEvent.getX());
                        }
                        Log.i(TAG, "DOWN");
                        if (scale > MIN_ZOOM) {
                            mode = Mode.DRAG;
                            startX = motionEvent.getX() - prevDx;
                            lastClick = motionEvent.getX();
                            startY = motionEvent.getY() - prevDy;
                            Log.d(TAG, "DOWN TO SCALE startX "+startX+" motionEvent.getX() "+motionEvent.getX()+" prevDx "+prevDx);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == Mode.DRAG) {
                            dx = motionEvent.getX() - startX;
                            dy = motionEvent.getY() - startY;
                            Log.d(TAG, "MOVE stratx " +String.valueOf(startX)+" dx "+String.valueOf(dx)+" motion x "+String.valueOf(motionEvent.getX()));
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
                        if(mSetScale!=null){
                            lastClick =motionEvent.getX();
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
                    float maxDy = (child().getHeight() - (child().getHeight() / scale))/ 2 * scale;
                    if(mSetScale!=null){
                        mSetScale.setMaxX((int)maxDx);
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
            scaledViewWidth = (int)(screenWidth*scale);
            if(mSetScale!=null){
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
        Log.d(TAG, "child().getLocationOnScreen(location) "+String.valueOf(location[0])+" "+String.valueOf(location[1]));
        Log.d(TAG, "scaledViewWidth: "+scaledViewWidth);
        Log.d(TAG, "screenWidth: "+screenWidth);
        calculateClickedSecond();



//        child().setTranslationY(dy);
    }

    void calculateClickedSecond(){
        float stastVisibleX = -location[0]+1;
        float endVisibleX =screenWidth + stastVisibleX-1;
        float startVisiblePercent = stastVisibleX/scaledViewWidth;
        float endVisiblePercent = endVisibleX/scaledViewWidth;
        Log.d(TAG, "stastVisibleX: "+stastVisibleX+" endVisibleX "+endVisibleX);
        Log.d(TAG, "startVisiblePercent: "+startVisiblePercent+" endVisiblePercent "+endVisiblePercent);
        float startVisibleSec = secsInDay * startVisiblePercent;
        float endVisibleSec = secsInDay * endVisiblePercent;
        Log.d(TAG, "startVisibleSec: "+startVisibleSec+" endVisibleSec "+endVisibleSec);
        float visibleSecs = endVisibleSec - (int)startVisibleSec;
        Log.d(TAG, "visibleSecs: "+visibleSecs);
        clickedSecond = startVisibleSec + (visibleSecs * (lastClick/screenWidth));
        Log.d(TAG, "clickedSecond: "+clickedSecond);
    }

    private View child() {
        return getChildAt(0);
    }

    public interface IZoomCallback{
        void setScale(float scale);
        void setTouchX(int x);
        void setMaxX(int maxX);
        void setMaxScreenWidth(int width);
        void secondClicked(float clickedSecond);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        screenWidth = getWidth();
        scaledViewWidth =screenWidth*scale;
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // толщина линии = 10

    }

    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        super.measureChildren(widthMeasureSpec, heightMeasureSpec);
    }
}
