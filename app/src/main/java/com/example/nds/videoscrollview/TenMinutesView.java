package com.example.nds.videoscrollview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class TenMinutesView extends View {
    public TenMinutesView(Context context, boolean hasRed) {
        super(context);
        init(hasRed);
    }

    public TenMinutesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TenMinutesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init (boolean hasRed){
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT,0.1f));
        setBackgroundColor(hasRed?getResources().getColor(R.color.colorAccent):getResources().getColor(R.color.colorPrimaryDark));
    }
}
