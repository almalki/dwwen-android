package com.dwwen.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.dwwen.common.ShipFasterApplication;

/**
 * Created by abdulaziz on 12/23/2014.
 */
public class HelveticaNeueTextView extends TextView {


    public HelveticaNeueTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public HelveticaNeueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HelveticaNeueTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        setTypeface(ShipFasterApplication.HelveticaNeueLT,1);
    }

}
