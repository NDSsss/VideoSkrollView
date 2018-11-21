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

    private ZoomLayout zoomLayout;
    private float mScale;
    private TextView et1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        zoomLayout =(ZoomLayout) findViewById(R.id.main_zoom);
        zoomLayout.setISetScaleListener(this);
        et1 = (TextView)findViewById(R.id.tv_info);
    }

    @Override
    protected void onResume() {
        super.onResume();
        zoomLayout.setProgress(ZoomLayout.SECS_IN_TIME/2);
    }

    @Override
    public void setScale(float scale) {
        mScale = scale;
        et1.setText("Scale "+String.valueOf(mScale));
    }

    @Override
    public void secondClicked(float clickedSecond) {
        et1.setText("Clicked on "+clickedSecond+" second");

    }
}