package com.example.nds.videoscrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ProgressLineView extends View {

    Paint paint = new Paint();

    int x;

    private void init() {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
    }

    public ProgressLineView(Context context) {
        super(context);
        init();
    }

    public ProgressLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawLine(canvas);
    }


    public void setLinesWiew( int x){
        this.x = x;
    }

    public void drawLine(Canvas canvas) {
        canvas.drawLine(x, 0, x, 400, paint);
    }
}
