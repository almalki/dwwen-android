package com.dwwen.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.dwwen.common.ShipFasterApplication;

public class FTextView extends TextView {

    public FTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        setTypeface(ShipFasterApplication.HelveticaNeueLT,1);

    }

}