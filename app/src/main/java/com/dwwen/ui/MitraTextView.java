package com.dwwen.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.dwwen.common.ShipFasterApplication;

/**
 * Created by abdulaziz on 12/23/2014.
 */
public class MitraTextView extends TextView {


    public MitraTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MitraTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MitraTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        setTypeface(ShipFasterApplication.Mitra,1);
    }

}
