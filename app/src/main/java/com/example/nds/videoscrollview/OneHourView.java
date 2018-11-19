package com.example.nds.videoscrollview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class OneHourView extends LinearLayout {
    public OneHourView(Context context) {
        super(context);
        init();
    }

    public OneHourView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OneHourView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                addView( new TenMinutesView(getContext(), true));
            } else {
                addView( new TenMinutesView(getContext(), false));
            }
        }
    }
}
