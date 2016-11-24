package com.example.ienning.android_art.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by ienning on 16-11-23.
 */

public class TestButton extends TextView {

    private static final String TAG = "Ienning";
    private int mLastX = 0;
    private int mLastY = 0;
    private int mScaledTouchSlop;

    public TestButton(Context context) {
        super(context);
        init();
    }

    public TestButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        Log.d(TAG, "init: sts: " + mScaledTouchSlop);
    }

    @SuppressLint("ClickAbleViewAccessiBility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                Log.d(TAG, "onTouchEvent: deltaX: " + deltaX + " deltaY: "+ deltaY);
                int translationX = (int)ViewHelper.getTranslationX(this) + deltaX;
                int translationY = (int) ViewHelper.getTranslationY(this) + deltaY;
                ViewHelper.setTranslationX(this, translationX);
                ViewHelper.setTranslationY(this, translationY);
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }
}
