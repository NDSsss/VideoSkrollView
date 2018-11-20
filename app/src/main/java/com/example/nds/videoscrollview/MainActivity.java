package com.example.nds.videoscrollview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.ViewGroupUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.util.Objects;

import static android.os.Build.VERSION_CODES.O;

public class MainActivity extends AppCompatActivity implements ZoomLayout.IZoomCallback{

    private ConstraintLayout constr;
    private View container;
    private ZoomLayout zoomLayout;
    private float mScale,mMaxX;
    private int SCREEN_MAX_WITH;
    public  ProgressLineView progressLineView;
    private TextView et1,et2,et3,et4;
    private LinearLayout llContainer1,llContainer2,llContainer3;
    private boolean changed = false;
    private Handler mHandler;
    private Runnable runner;
    int curProgress = 0;
    public int max = 0;
    public ViewUtils viewUtils;
    public ViewGroupUtils viewGroupUtils;
    View line;
    private static final String TAG = "MainView: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mHandler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        line = findViewById(R.id.view_progress_line);
        zoomLayout =(ZoomLayout) findViewById(R.id.main_zoom);
        zoomLayout.setISetScaleListener(this);
        et1 = (TextView)findViewById(R.id.tv_info);
    }

    private void resizeView(View view, int newWidth, int newHeight) {
        try {
            Constructor<? extends ViewGroup.LayoutParams> ctor = view.getLayoutParams().getClass().getDeclaredConstructor(int.class, int.class);

            view.setLayoutParams(ctor.newInstance(newWidth, view.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runJustBeforeBeingDrawn(final View view) {
        final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                view.getViewTreeObserver().removeOnPreDrawListener(this);
//                Log.d(TAG, "X =================================== >" + view.getX());
//                Log.d(TAG, "scaleX =================================== >" + view.getScaleX());
//                Log.d(TAG, "cProgress =================================== >" + view.getScrollX());
                return true;
            }
        };
        view.getViewTreeObserver().addOnPreDrawListener(preDrawListener);
    }

    @Override
    public void setScale(float scale) {
//        mScale = scale;
//        if(et1!=null) {
//            et1.setText(String.valueOf(mScale));
//        }
//        et2.setText(String.valueOf(mScale));
//        et3.setText(String.valueOf(mScale));
//        et4.setText(String.valueOf(mScale));
//        if(mScale>2&&!changed){
//            llContainer1.removeAllViews();
//            llContainer1.addView(new OneHourView(getApplicationContext()));
//            changed = true;
//        }

        mScale = scale;

    }

    @Override
    public void setTouchX(int x) {
        et1.setText("click x "+String.valueOf(x));
    }

    @Override
    public void setMaxX(int maxX) {
        mMaxX = maxX;
        et1.setText("Scale "+String.valueOf(mScale)+" MaxX "+String.valueOf(maxX));
    }


    @Override
    public void setMaxScreenWidth(int width) {
        SCREEN_MAX_WITH = width;
    }

    @Override
    public void secondClicked(float clickedSecond) {

    }
}