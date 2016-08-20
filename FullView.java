package com.kang.hola.vidio_test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 * Created by Kang_Note on 2016-08-18.
 */
public class FullView extends VideoView {

    public FullView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        Display dis =((WindowManager)getContext().
                getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        setMeasuredDimension(dis.getWidth(), dis.getHeight());
    }
}
