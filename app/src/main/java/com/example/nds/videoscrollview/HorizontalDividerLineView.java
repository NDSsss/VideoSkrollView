package com.example.nds.videoscrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class HorizontalDividerLineView extends View {


    int zoom = 0;
    int wndowSize = 0;
    float startX = 0, startY = 0, stopX = 0, stopY = 0;
    float startX1 = 0, startY1 = 0, stopX1 = 0, stopY1 = 0;
    float startX2 = 0, startY2 = 0, stopX2 = 0, stopY2 = 0;
    float startX3 = 0, startY3 = 0, stopX3 = 0, stopY3 = 0;
    float startX4 = 0, startY4 = 0, stopX4 = 0, stopY4 = 0;
    float startX5 = 0, startY5 = 0, stopX5 = 0, stopY5 = 0;
    float startX6 = 0, startY6 = 0, stopX6 = 0, stopY6 = 0;
    float startX7 = 0, startY7 = 0, stopX7 = 0, stopY7 = 0;
    float startX8 = 0, startY8 = 0, stopX8 = 0, stopY8 = 0;
    float startX9 = 0, startY9 = 0, stopX9 = 0, stopY9 = 0;

    Paint paintV = new Paint();
    Paint paintH = new Paint();
    Paint paintHL = new Paint();
    int [ ] points;

    private void init() {

        paintV.setARGB(255, 57, 67, 79);
        paintV.setStrokeWidth(75);

        paintH.setARGB(255, 35, 47, 64);
        paintH.setStrokeWidth(150);

        paintHL.setARGB(255, 255, 255, 255);
        paintHL.setStrokeWidth(1f);

    }

    public HorizontalDividerLineView(Context context) {
        super(context);
        init();
    }

    public HorizontalDividerLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalDividerLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setLinesWiew(int [ ] data, Canvas canvas){
        this.points = data;
        System.arraycopy(data, 0, points, 0, data.length);
    }

    @Override
    public void onDraw(Canvas canvas) {

        switch (zoom){
            case 1: for(int i = 0; i < 8; i++){
                        canvas.drawLine( wndowSize, 750, 300, 900, paintHL);
                    }
            break;
        }

        canvas.drawLine(600, 750, 600, 900, paintHL);
        canvas.drawLine(900, 750, 900, 900, paintHL);
        canvas.drawLine(1200, 750, 1200, 900, paintHL);
        canvas.drawLine(1500, 750, 1500, 900, paintHL);
        canvas.drawLine(1800, 750, 1800, 900, paintHL);
        canvas.drawLine(2100, 750, 2400, 900, paintHL);

    }

}
